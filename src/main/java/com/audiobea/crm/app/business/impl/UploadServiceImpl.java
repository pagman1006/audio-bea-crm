package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInDemographicExcelFile;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.dto.DtoInProductExcelFile;
import com.audiobea.crm.app.commons.mapper.ProductMapper;
import com.audiobea.crm.app.core.exception.NoSuchFileException;
import com.audiobea.crm.app.core.exception.ParseFileException;
import com.audiobea.crm.app.core.exception.UploadFileException;
import com.audiobea.crm.app.dao.demographic.ICityDao;
import com.audiobea.crm.app.dao.demographic.IColonyDao;
import com.audiobea.crm.app.dao.demographic.IStateDao;
import com.audiobea.crm.app.dao.demographic.model.City;
import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.dao.demographic.model.State;
import com.audiobea.crm.app.dao.product.IBrandDao;
import com.audiobea.crm.app.dao.product.IProductDao;
import com.audiobea.crm.app.dao.product.IProductTypeDao;
import com.audiobea.crm.app.dao.product.ISubBrandDao;
import com.audiobea.crm.app.dao.product.model.Brand;
import com.audiobea.crm.app.dao.product.model.Product;
import com.audiobea.crm.app.dao.product.model.ProductType;
import com.audiobea.crm.app.dao.product.model.SubBrand;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UploadServiceImpl implements IUploadService {

    private static final DecimalFormat decimalF = new DecimalFormat("00.00");
    private static final DecimalFormat df = new DecimalFormat("#,###");

    private static Integer statesCount = 0;
    private static Integer citiesCount = 0;
    private static Integer coloniesCount = 0;

    private IStateDao stateDao;
    private ICityDao cityDao;
    private IColonyDao colonyDao;
    private IProductDao productDao;
    private IBrandDao brandDao;
    private ISubBrandDao subBrandDao;
    private IProductTypeDao productTypeDao;
    private MessageSource messageSource;
    private ProductMapper productMapper;

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathImage = getPath(filename);
        Resource resource;
        resource = new UrlResource(pathImage.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new UploadFileException(
                    Utils.getLocalMessage(messageSource, I18Constants.UPLOAD_FILE_EXCEPTION.getKey(), filename));
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
    public void delete(String filename) throws NoSuchFileException {
        Path rootPath = getPath(filename);
        File file = rootPath.toFile();
        if (file.exists() && file.canRead()) {
            try {
                Files.delete(rootPath);
            } catch (IOException e) {
                throw new NoSuchFileException(Utils.getLocalMessage(messageSource, I18Constants.NO_FILE_FOUND.getKey(), filename));
            }
        }
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
        Utils.hasExcelFormat(file, messageSource);
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

    @Override
    public ResponseData<DtoInProduct> uploadProducts(MultipartFile file) {
        long timeStart = new Date().getTime();
        Utils.hasExcelFormat(file, messageSource);
        try {
            List<DtoInProduct> listProducts = excelToListProducts(file.getInputStream());
            long timeUpload = new Date().getTime();
            String strTimeUpload = getFinishTimeStr(timeStart, timeUpload);
            if (listProducts != null && !listProducts.isEmpty()){
                log.debug("Products loaded: {}", listProducts.size());
            }
            log.debug("Upload time: {}", strTimeUpload);
            return new ResponseData<>(listProducts, null, null, null, null);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<DtoInProduct> excelToListProducts(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(Constants.SHEET);
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            List<Product> listProducts = new ArrayList<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) { // Skip header
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                DtoInProductExcelFile file = reedCellsProduct(cellsInRow);
                Brand brand = getBrandFromFile(file);
                SubBrand subBrand = getSubBrandFromFile(file);
                if (subBrand != null && brand != null) {
                    subBrand.setBrandId(brand.getId());
                    subBrandDao.save(subBrand);
                    brand.getSubBrands().add(subBrand);
                    brandDao.save(brand);
                }

                ProductType productType = getProductTypeFromFile(file);
                String brandId = brand == null ? null : brand.getId();
                String subBrandId = subBrand == null ? null : subBrand.getId();
                Product product = getProductFromFile(file, brandId, subBrandId, productType);
                if (subBrand != null && brand != null && product != null) {
                    subBrand.setBrandId(brand.getId());
                    subBrand.getProducts().add(product);
                    subBrandDao.save(subBrand);

                    brand.getSubBrands().add(subBrand);
                    brand.getProducts().add(product);
                    brandDao.save(brand);
                }
                listProducts.add(product);
            }
            return listProducts.isEmpty()? null
                    : listProducts.stream().map(productMapper::productToDtoInProduct).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ParseFileException(Utils.getLocalMessage(messageSource, I18Constants.FAIL_PARSE_EXCEL_FILE.getKey(), Constants.SHEET));
        }
    }

    private DtoInProductExcelFile reedCellsProduct(Iterator<Cell> cellsInRow) {
        DtoInProductExcelFile file = new DtoInProductExcelFile();
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            switch (cellIdx) {
                case 0:
                    log.debug("getBrand");
                    file.setBrand(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getBrand());
                    break;
                case 1:
                    log.debug("getSubBrand");
                    file.setSubBrand(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getSubBrand());
                    break;
                case 2:
                    log.debug("getDescription");
                    file.setDescription(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getDescription());
                    break;
                case 3:
                    log.debug("getDiscount");
                    file.setDiscount(currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getDiscount()));
                    break;
                case 4:
                    log.debug("getEnabled");
                    file.setEnabled(currentCell.getNumericCellValue() == 1);
                    log.debug(String.valueOf(file.getEnabled()));
                    break;
                case 5:
                    log.debug("getProductNew");
                    file.setProductNew(currentCell.getNumericCellValue() == 1);
                    log.debug(String.valueOf(file.getProductNew()));
                    break;
                case 6:
                    log.debug("getPrice");
                    file.setPrice(currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getPrice()));
                    break;
                case 7:
                    log.debug("getProductName");
                    file.setProductName(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getProductName());
                    break;
                case 8:
                    log.debug("getStock");
                    file.setStock((int) currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getStock()));
                    break;
                case 9:
                    log.debug("getTitle");
                    file.setTitle(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getTitle());
                    break;
                case 10:
                    log.debug("getType");
                    file.setType(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getType());
                    break;
                default:
                    break;
            }
            cellIdx++;
        }
        return file;
    }

    private Brand getBrandFromFile(DtoInProductExcelFile file) {
        if (file.getBrand() == null || file.getBrand().isEmpty()) {
            return null;
        }
        Brand brand = brandDao.findByBrandName(file.getBrand()).orElse(null);
        if (brand == null) {
            brand = new Brand();
            brand.setBrandName(file.getBrand());
            brand.setSubBrands(new ArrayList<>());
            brand.setProducts(new ArrayList<>());
            return brandDao.save(brand);
        }
        return brand;
    }

    private SubBrand getSubBrandFromFile(DtoInProductExcelFile file) {
        if (file.getSubBrand() == null || file.getSubBrand().isEmpty()) {
            return null;
        }
        SubBrand subBrand = subBrandDao.findBySubBrandName(file.getSubBrand()).orElse(null);
        if (subBrand == null) {
            subBrand = new SubBrand();
            subBrand.setSubBrandName(file.getSubBrand());
            subBrand.setProducts(new ArrayList<>());
            return subBrandDao.save(subBrand);
        }
        return subBrand;
    }

    private ProductType getProductTypeFromFile(DtoInProductExcelFile file) {
        if (file == null || file.getType() == null || file.getType().isEmpty()) {
            return null;
        }
        ProductType productType = productTypeDao.findProductTypeByType(file.getType()).orElse(null);
        if (productType == null) {
            productType = new ProductType();
            productType.setType(file.getType());
            return productTypeDao.save(productType);
        }
        return productType;
    }

    private Product getProductFromFile(DtoInProductExcelFile file, String brandId, String subBrandId, ProductType productType) {
        if (file == null || file.getProductName() == null || file.getProductName().isEmpty()) {
            return null;
        }
        Product product = new Product();
        product.setProductNew(file.getProductNew());
        product.setPrice(file.getPrice());
        product.setDiscount(file.getDiscount());
        product.setTitle(file.getTitle());
        product.setDescription(file.getDescription());
        product.setStock(file.getStock());
        product.setProductType(productType);
        product.setBrandId(brandId);
        product.setSubBrandId(subBrandId);
        return productDao.save(product);
    }

    private void saveColoniesAsync(List<State> listStates, int totalElements) {
        new Thread(() -> {
            long timeStart = new Date().getTime();
            log.debug("Start load to DBB");
            log.debug("0.00%");
            double elements = 0;
            for (State state : listStates) {
                // Save state, save city, save colony
                State st = stateDao.save(state);
                for (City city : st.getCities()) {
                    city.setStateId(st.getId());
                    City ct = cityDao.save(city);
                    for (Colony colony : ct.getColonies()) {
                        colony.setCityId(ct.getId());
                        colony.setStateId(st.getId());
                    }
                    city.setColonies(colonyDao.saveAll(ct.getColonies()));
                }
                st.setCities(cityDao.saveAll(st.getCities()));
                stateDao.save(st);
                elements += state.getCities().stream().mapToInt(city -> city.getColonies().size()).sum();
                double progress = (elements * 100) / (double) totalElements;
                log.debug("{}% -> {}, {}/{}", decimalF.format(progress), state.getName(), df.format(elements), df.format(totalElements));
            }
            long timeFinish = new Date().getTime();
            String totalTimeSave = getFinishTimeStr(timeStart, timeFinish);
            log.debug("Saved time: {}", totalTimeSave);
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
                    setupCity(city);
                    count += city.getColonies().size();
                    coloniesCount = coloniesCount + city.getColonies().size();
                    citiesCount++;
                }
                log.debug("{} -> cities: {}, colonies: {}", state.getName(), state.getCities().size(), count);
            }
            statesCount++;
        }
        return listState;
    }

    private void setupCity(City city) {
        List<Colony> colonies = new ArrayList<>(city.getColonies());
        Collections.sort(colonies);
        city.setColonies(colonies);
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
                DtoInDemographicExcelFile file = reedCells(cellsInRow);
                String nameState = file.getState();
                String nameCity = file.getCity();
                String nameColony = file.getColony();
                String codePostal = file.getCodePostal();
                setStateFromSetStates(listSetStates, nameState, nameCity, nameColony, codePostal);
            }
            return listSetStates;
        } catch (IOException e) {
            throw new ParseFileException(Utils.getLocalMessage(messageSource, I18Constants.FAIL_PARSE_EXCEL_FILE.getKey(), Constants.SHEET));
        }
    }

    private void setStateFromSetStates(Set<State> listSetStates, String nameState, String nameCity, String nameColony, String codePostal) {
        State state = listSetStates.stream().filter(s -> s.getName().equals(nameState)).findFirst().orElse(null);

        if (state == null) {
            state = new State();
            state.setName(nameState);
            state.setCities(new ArrayList<>());
            listSetStates.add(state);
        }
        City city = setCity(state, nameCity);
        city.getColonies().add(setColony(nameColony, codePostal));
    }

    private DtoInDemographicExcelFile reedCells(Iterator<Cell> cellsInRow) {
        DtoInDemographicExcelFile file = new DtoInDemographicExcelFile();
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
            city = state.getCities().stream().filter(c -> c.getName().equals(nameCity)).findFirst().orElse(null);
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
    public List<String> uploadFiles(MultipartFile[] files) {
        return Arrays.stream(files).map(file -> {
            try {
                log.debug("file: {}", file.getName());
                return copy(file);
            } catch (IOException e) {
                log.error(e.getMessage());
                return null;
            }
        }).collect(Collectors.toList());
    }

}
