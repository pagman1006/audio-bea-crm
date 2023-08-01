package com.audiobea.crm.app.business.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import com.audiobea.crm.app.dao.customer.IStateDao;
import com.audiobea.crm.app.dao.customer.model.City;
import com.audiobea.crm.app.dao.customer.model.Colony;
import com.audiobea.crm.app.dao.customer.model.State;
import com.audiobea.crm.app.exception.NoSuchElementsFoundException;
import com.audiobea.crm.app.utils.ExcelHelper;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UploadServiceImpl implements IUploadService {

    private static final String UPLOADS_FOLDER = "uploads";
    public static String[] HEADERs = {"id", "cp", "colonia", "ciudad", "estado"};
    public static String SHEET = "data";

    private final MessageSource messageSource;

    @Autowired
    private IStateDao stateDao;

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathImage = getPath(filename);
        Resource resource = null;
        resource = new UrlResource(pathImage.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Error: no se puede cargar la imagen: " + pathImage);
        }
        return resource;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path rootPath = getPath(uniqueFileName);
        Files.copy(file.getInputStream(), rootPath);
        return uniqueFileName;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPath = getPath(filename);
        File file = rootPath.toFile();
        return file.exists() && file.canRead() && file.delete();
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    @Override
    public void init() throws IOException {
        Files.createDirectory(Paths.get(UPLOADS_FOLDER));
    }

    @Override
    public DtoInFileResponse uploadExcelFile(MultipartFile file) {
        long timeStart = new Date().getTime();
        DtoInFileResponse response = new DtoInFileResponse();
        if (!ExcelHelper.hasExcelFormat(file)) {
            // TODO Implements Exception
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
        try {
            List<State> listStates = new ArrayList<>(excelToListStates(file.getInputStream()));
            Collections.sort(listStates);

            int statesCount = 0;
            int citiesCount = 0;
            int coloniesCount = 0;

            for (State state : listStates) {
                List<City> cities = new ArrayList<>(state.getCities());
                Collections.sort(cities);
                state.setCities(cities);
                if (state.getCities() != null && !state.getCities().isEmpty()) {
                    int count = 0;
                    for (City city : state.getCities()) {
                        List<Colony> colonies = new ArrayList<>(city.getColonies());
                        Collections.sort(colonies);
                        city.setColonies(colonies);
                        count = count + city.getColonies().size();
                        coloniesCount = coloniesCount + city.getColonies().size();
                        citiesCount++;
                    }
                    log.debug("State {}, cities loaded: {}, colonies: {}", state.getName(), state.getCities().size(),
                            count);
                }
                statesCount++;
            }
            long timeUpload = new Date().getTime();
            String strTimeUpload = getFinishTimeStr(timeStart, timeUpload);
            response.setStates((long) statesCount);
            response.setCities((long) citiesCount);
            response.setColonies((long) coloniesCount);

            log.debug("States loaded: {}", statesCount);
            log.debug("Cities loaded: {}", citiesCount);
            log.debug("Colonies loaded: {}", coloniesCount);

            log.debug("Upload time: {}", strTimeUpload);

            stateDao.saveAll(listStates);

            long timeSave = new Date().getTime();
            String strSaveTime = getFinishTimeStr(timeUpload, timeSave);
            String strTotalTime = getFinishTimeStr(timeStart, timeSave);

            log.debug("Save time: {}", strSaveTime);
            log.debug("Total time: {}", strTotalTime);
            log.debug("Upload finished!!!");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return response;
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
        return String.valueOf(minutes).concat(":")
                .concat(String.valueOf(seconds).concat(":").concat(String.valueOf(milliSeconds)));
    }

    public Set<State> excelToListStates(InputStream input) {
        try (Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;

            Set<State> listSetStates = new HashSet<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                DtoInFileExcel file = new DtoInFileExcel();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 1 -> file.setCodePostal(Utils.removeAccents(currentCell.getStringCellValue()));
                        case 2 -> file.setColony(Utils.removeAccents(currentCell.getStringCellValue()));
                        case 3 -> file.setCity(Utils.removeAccents(currentCell.getStringCellValue()));
                        case 4 -> file.setState(Utils.removeAccents(currentCell.getStringCellValue()));
                        default -> {
                        }
                    }
                    cellIdx++;
                }


                // Colony -> City -> State
                String nameState = file.getState();
                String nameCity = file.getCity();
                String nameColony = file.getColony();
                String codePostal = file.getCodePostal();

                State state = listSetStates.stream().filter(s -> s.getName().equals(nameState))
                        .toList().stream().findFirst().orElse(null);

                if (state == null) {
                    state = new State();
                    state.setName(nameState);
                    state.setCities(new ArrayList<>());
                    listSetStates.add(state);
                }

                City city = null;
                if (state.getCities() != null && !state.getCities().isEmpty()) {
                    city = state.getCities().stream().filter(c -> c.getName().equals(nameCity))
                            .toList().stream().findFirst().orElse(null);
                }

                if (city == null) {
                    city = new City();
                    city.setName(nameCity);
                    city.setColonies(new ArrayList<>());
                    state.getCities().add(city);
                }

                Colony colony = new Colony();
                colony.setName(nameColony);
                colony.setPostalCode(codePostal);
                city.getColonies().add(colony);
            }
            return listSetStates;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }

}
