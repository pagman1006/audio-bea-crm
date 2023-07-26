package com.audiobea.crm.app.business.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IColonyService;
import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.dto.DtoInFileExcel;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.controller.mapper.ColonyMapper;
import com.audiobea.crm.app.controller.mapper.ListColonyMapper;
import com.audiobea.crm.app.dao.customer.IStateDao;
import com.audiobea.crm.app.dao.customer.model.City;
import com.audiobea.crm.app.dao.customer.model.Colony;
import com.audiobea.crm.app.dao.customer.model.State;
import com.audiobea.crm.app.utils.ExcelHelper;

@Service
public class UploadServiceImpl implements IUploadService {

	private final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

	private static final String UPLOADS_FOLDER = "uploads";
	
	@Autowired
	private IStateDao stateDao;

	@Autowired
	private IColonyService colonyService;

	@Autowired
	private ListColonyMapper listColonyMapper;

	@Autowired
	private ColonyMapper colonyMapper;

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathImage = getPath(filename);
		Resource resource = null;
		resource = new UrlResource(pathImage.toUri());
		if (!resource.exists() || !resource.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathImage.toString());
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File file = rootPath.toFile();
		if (file.exists() && file.canRead() && file.delete()) {
			return true;
		}
		return false;
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
		try {
			List<DtoInFileExcel> list = ExcelHelper.excelToColonies(file.getInputStream());
			Set<State> listSetStates = new HashSet<>();
			Set<City> listSetCities = new HashSet<>();

			for (DtoInFileExcel dto : list) {
				String nameState = dto.getState();
				String nameCity = dto.getCity();
				String nameColony = dto.getColony();
				String codePostal = dto.getCodePostal();

				State state = listSetStates.stream().filter(s -> s.getName().equals(nameState))
						.collect(Collectors.toList()).stream().findFirst().orElse(null);

				if (state == null) {
					state = new State();
					state.setName(nameState);
					state.setCities(new ArrayList<>());
					listSetStates.add(state);
				}

				City city = null;
				if (state.getCities() != null && !state.getCities().isEmpty()) {
					city = state.getCities().stream().filter(c -> c.getName().equals(nameCity))
							.collect(Collectors.toList()).stream().findFirst().orElse(null);
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
			
			List<State> listStates = new ArrayList<>(listSetStates.stream().toList());
			Collections.sort(listStates);
			
			logger.debug("States loaded: {}", listStates.size());
			for (State state : listStates) {
				List<City> cities = new ArrayList<>(state.getCities());
				Collections.sort(cities);
				state.setCities(cities);
				if (state.getCities() != null && !state.getCities().isEmpty()) {
					int countColonies = 0;
					for (City city: state.getCities()) {
						List<Colony> colonies = new ArrayList<>(city.getColonies());
						Collections.sort(colonies);
						city.setColonies(colonies);
						countColonies = countColonies + city.getColonies().size();
					}
					logger.debug("State {}, cities loaded: {}, colonies: {}", state.getName(), state.getCities().size(), countColonies);
				}
			}
			stateDao.saveAll(listStates);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

}
