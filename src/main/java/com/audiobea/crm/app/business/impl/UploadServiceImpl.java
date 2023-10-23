package com.audiobea.crm.app.business.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.dto.DtoInFileExcel;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.core.exception.NoSuchFileException;
import com.audiobea.crm.app.core.exception.ParseFileException;
import com.audiobea.crm.app.core.exception.UploadFileException;
import com.audiobea.crm.app.core.exception.ValidFileException;
import com.audiobea.crm.app.dao.demographic.IStateDao;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.dao.demographic.model.State;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.ExcelHelper;
import com.audiobea.crm.app.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadServiceImpl implements IUploadService {

	@Autowired
	private MessageSource messageSource;

	private Integer statesCount = 0;
	private Integer citiesCount = 0;
	private Integer coloniesCount = 0;

	private static final DecimalFormat decimalF = new DecimalFormat("00.00");
	private static final DecimalFormat df = new DecimalFormat("#,###");

	@Autowired
	private IStateDao stateDao;

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathImage = getPath(filename);
		Resource resource = null;
		resource = new UrlResource(pathImage.toUri());
		if (!resource.exists() || !resource.isReadable()) {
			throw new UploadFileException(Utils.getLocalMessage(messageSource, I18Constants.UPLOAD_FILE_EXCEPTION.getKey(), filename));
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		log.debug("uniqueFileName: {}", uniqueFileName);
		Path rootPath = getPath(uniqueFileName);
		log.debug("rootPath: {}", rootPath.getFileName());
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) throws NoSuchFileException {
		Path rootPath = getPath(filename);
		File file = rootPath.toFile();
		if (file.exists() && file.canRead()) {
			try {
				Files.delete(rootPath);
			} catch (IOException e) {
				throw new NoSuchFileException(Utils.getLocalMessage(messageSource, I18Constants.NO_FILE_FOUND.getKey(), filename));
			}
		}
		return true;
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(Constants.UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(Constants.UPLOADS_FOLDER));
	}

	@Override
	public DtoInFileResponse uploadExcelFile(MultipartFile file) {
		long timeStart = new Date().getTime();
		if (file == null || !ExcelHelper.hasExcelFormat(file)) {
			throw new ValidFileException(Utils.getLocalMessage(messageSource, I18Constants.NOT_VALID_EXCEL.getKey()));
		}
		statesCount = 0;
		citiesCount = 0;
		coloniesCount = 0;

		try {
			List<State> listStates = setupListStates(excelToListStates(file.getInputStream()));
			long timeUpload = new Date().getTime();
			String strTimeUpload = getFinishTimeStr(timeStart, timeUpload);
			log.debug("States loaded: {}", statesCount);
			log.debug("Cities loaded: {}", citiesCount);
			log.debug("Colonies loaded: {}", coloniesCount);
			log.debug("Upload time: {}", strTimeUpload);

			saveColoniesAsync(listStates, coloniesCount);
			return new DtoInFileResponse(df.format(statesCount), df.format(citiesCount), df.format(coloniesCount));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	private void saveColoniesAsync(List<State> listStates, int totalElements) {
		new Thread(() -> {
			log.debug("Start load to DBB");
			log.debug("0.00%");
			double elements = 0;
			for (State state : listStates) {
				stateDao.save(state);
				elements += state.getCities().stream().mapToInt(city -> city.getColonies().size()).sum();
				double avance = (elements * 100) / Double.valueOf(totalElements);
				log.debug("{}% -> {}, {}/{}", decimalF.format(avance), state.getName(), df.format(elements), df.format(totalElements));
			}
		}).start();
	}

	private List<State> setupListStates(Set<State> setState) {
		List<State> listState = new ArrayList<>(setState);
		Collections.sort(listState);
		for (State state : listState) {
			List<City> cities = new ArrayList<>(state.getCities());
			Collections.sort(cities);
			state.setCities(cities);
			if (state.getCities() != null && !state.getCities().isEmpty()) {
				int count = 0;
				for (City city : state.getCities()) {
					city = setupCity(city);
					count = count + city.getColonies().size();
					coloniesCount = coloniesCount + city.getColonies().size();
					citiesCount++;
				}
				log.debug("{} -> cities: {}, colonies: {}", state.getName(), state.getCities().size(), count);
			}
			statesCount++;
		}
		return listState;
	}

	private City setupCity(City city) {
		List<Colony> colonies = new ArrayList<>(city.getColonies());
		Collections.sort(colonies);
		city.setColonies(colonies);
		return city;
	}

	private String getFinishTimeStr(long timeStart, long timeFinish) {
		if (timeStart >= timeFinish) {
			return "";
		}
		long total = timeFinish - timeStart;
		int seconds = (int) (total / 1000);

		int minutes = seconds / 60;
		seconds = seconds - (minutes * 60);

		int milliSeconds = (int) total - (seconds * 1000);
		return String.valueOf(minutes).concat(":").concat(String.valueOf(seconds).concat(":").concat(String.valueOf(milliSeconds)));
	}

	public Set<State> excelToListStates(InputStream input) {
		try (Workbook workbook = new XSSFWorkbook(input)) {
			Sheet sheet = workbook.getSheet(Constants.SHEET);
			Iterator<Row> rows = sheet.iterator();
			int rowNumber = 0;
			Set<State> listSetStates = new HashSet<>();
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				if (rowNumber == 0) { // Skip header
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();
				DtoInFileExcel file = reedCells(cellsInRow);
				String nameState = file.getState();
				String nameCity = file.getCity();
				String nameColony = file.getColony();
				String codePostal = file.getCodePostal();
				setStateFromSetStates(listSetStates, nameState, nameCity, nameColony, codePostal);
			}
			return listSetStates;
		} catch (IOException e) {
			throw new ParseFileException(
					Utils.getLocalMessage(messageSource, I18Constants.FAIL_PARSE_EXCEL_FILE.getKey(), Constants.SHEET));
		}
	}

	private State setStateFromSetStates(Set<State> listSetStates, String nameState, String nameCity, String nameColony, String codePostal) {
		State state = listSetStates.stream().filter(s -> s.getName().equals(nameState)).collect(Collectors.toList()).stream().findFirst()
				.orElse(null);

		if (state == null) {
			state = new State();
			state.setName(nameState);
			state.setCities(new ArrayList<>());
			listSetStates.add(state);
		}
		City city = setCity(state, nameCity);
		city.getColonies().add(setColony(nameColony, codePostal));
		return state;
	}

	private DtoInFileExcel reedCells(Iterator<Cell> cellsInRow) {
		DtoInFileExcel file = new DtoInFileExcel();
		int cellIdx = 0;
		while (cellsInRow.hasNext()) {
			Cell currentCell = cellsInRow.next();
			switch (cellIdx) {
			case 1:
				file.setCodePostal(Utils.removeAccents(currentCell.getStringCellValue()));
				break;
			case 2:
				file.setColony(Utils.removeAccents(currentCell.getStringCellValue()));
				break;
			case 3:
				file.setCity(Utils.removeAccents(currentCell.getStringCellValue()));
				break;
			case 4:
				file.setState(Utils.removeAccents(currentCell.getStringCellValue()));
				break;
			default:
				break;
			}
			cellIdx++;
		}
		return file;
	}

	private City setCity(State state, String nameCity) {
		City city = null;
		if (state.getCities() != null && !state.getCities().isEmpty()) {
			city = state.getCities().stream().filter(c -> c.getName().equals(nameCity)).collect(Collectors.toList()).stream().findFirst()
					.orElse(null);
		}
		if (city == null) {
			city = new City();
			city.setName(nameCity);
			city.setColonies(new ArrayList<>());
			state.getCities().add(city);
		}
		return city;
	}

	private Colony setColony(String nameColony, String codePostal) {
		Colony colony = new Colony();
		colony.setName(nameColony);
		colony.setPostalCode(codePostal);
		return colony;
	}

	public Path getPath(String filename) {
		return Paths.get(Constants.UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public List<String> uploadFiles(MultipartFile[] files) throws IOException {
		return Arrays.asList(files).stream().map(file -> {
			try {
				log.debug("file: {}", file.getName());
				return copy(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

}
