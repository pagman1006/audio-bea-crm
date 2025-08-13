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

import static com.audiobea.crm.app.utils.ConstantsLog.*;

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
    public Resource load(final String filename) throws MalformedURLException {
        final Path pathImage = getPath(filename);
        final Resource resource = new UrlResource(pathImage.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new UploadFileException(
                    Utils.getLocalMessage(messageSource, I18Constants.UPLOAD_FILE_EXCEPTION.getKey(), filename));
        }
        return resource;
    }

    @Override
    public String copy(final MultipartFile file) throws IOException {
        final String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.debug(LOG_FILE_NAME, uniqueFileName);
        final Path rootPath = getPath(uniqueFileName);
        log.debug(LOG_ROOT_PATH, rootPath.getFileName());
        Files.copy(file.getInputStream(), rootPath);
        return uniqueFileName;
    }

    @Override
    public void delete(final String filename) throws NoSuchFileException {
        final Path rootPath = getPath(filename);
        final File file = rootPath.toFile();
        if (file.exists() && file.canRead()) {
            try {
                Files.delete(rootPath);
            } catch (IOException e) {
                throw new NoSuchFileException(
                        Utils.getLocalMessage(messageSource, I18Constants.NO_FILE_FOUND.getKey(), filename));
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
    public DtoInFileResponse uploadExcelFile(final MultipartFile file) {
        long timeStart = new Date().getTime();
        Utils.hasExcelFormat(file, messageSource);
        statesCount = 0;
        citiesCount = 0;
        coloniesCount = 0;

        try {
            final List<State> listStates = setupListStates(excelToListStates(file.getInputStream()));
            long timeUpload = new Date().getTime();
            final String strTimeUpload = getFinishTimeStr(timeStart, timeUpload);
            log.debug(LOG_STATES_LOADED, statesCount);
            log.debug(LOG_CITIES_LOADED, citiesCount);
            log.debug(LOG_COLONIES_LOADED, coloniesCount);
            log.debug(LOG_UPLOAD_TIME, strTimeUpload);
            saveColoniesAsync(listStates, coloniesCount);
            return new DtoInFileResponse(df.format(statesCount), df.format(citiesCount), df.format(coloniesCount));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<State> setupListStates(final Set<State> setState) {
        final List<State> listState = new ArrayList<>(setState);
        Collections.sort(listState);
        for (State state : listState) {
            final List<City> cities = new ArrayList<>(state.getCities());
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
                log.debug(LOG_CITIES_COLONIES_COUNT, state.getName(), state.getCities().size(), count);
            }
            statesCount++;
        }
        return listState;
    }

    public Set<State> excelToListStates(final InputStream input) {
        try (Workbook workbook = new XSSFWorkbook(input)) {
            final Sheet sheet = workbook.getSheet(Constants.SHEET);
            final Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            final Set<State> listSetStates = new HashSet<>();
            while (rows.hasNext()) {
                final Row currentRow = rows.next();
                if (rowNumber == 0) { // Skip header
                    rowNumber++;
                    continue;
                }
                final Iterator<Cell> cellsInRow = currentRow.iterator();
                final DtoInDemographicExcelFile file = reedCells(cellsInRow);
                final String nameState = file.getState();
                final String nameCity = file.getCity();
                final String nameColony = file.getColony();
                final String codePostal = file.getCodePostal();
                setStateFromSetStates(listSetStates, nameState, nameCity, nameColony, codePostal);
            }
            return listSetStates;
        } catch (IOException e) {
            throw new ParseFileException(
                    Utils.getLocalMessage(messageSource, I18Constants.FAIL_PARSE_EXCEL_FILE.getKey(), Constants.SHEET));
        }
    }

    private String getFinishTimeStr(final long timeStart, final long timeFinish) {
        if (timeStart >= timeFinish) {
            return "";
        }
        final long total = timeFinish - timeStart;
        int seconds = (int) (total / 1000);

        final int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);

        final int milliSeconds = (int) total - (seconds * 1000);
        return String.valueOf(minutes).concat(":")
                .concat(String.valueOf(seconds).concat(":").concat(String.valueOf(milliSeconds)));
    }

    private void saveColoniesAsync(final List<State> listStates, final int totalElements) {
        new Thread(() -> {
            final long timeStart = new Date().getTime();
            log.debug(LOG_START_LOAD_TO_DB);
            log.debug(LOG_0_00);
            double elements = 0;
            for (State state : listStates) {
                final State st = stateDao.save(state);
                for (City city : st.getCities()) {
                    city.setStateId(st.getId());
                    final City ct = cityDao.save(city);
                    for (Colony colony : ct.getColonies()) {
                        colony.setCityId(ct.getId());
                        colony.setStateId(st.getId());
                    }
                    city.setColonies(colonyDao.saveAll(ct.getColonies()));
                }
                st.setCities(cityDao.saveAll(st.getCities()));
                stateDao.save(st);
                elements += state.getCities().stream().mapToInt(city -> city.getColonies().size()).sum();
                final double progress = (elements * 100) / (double) totalElements;
                log.debug(LOG_FORMAT_PROGRESS, decimalF.format(progress), state.getName(), df.format(elements),
                        df.format(totalElements));
            }
            final long timeFinish = new Date().getTime();
            final String totalTimeSave = getFinishTimeStr(timeStart, timeFinish);
            log.debug(LOG_SAVED_TIME, totalTimeSave);
        }).start();
    }

    private void setupCity(final City city) {
        final List<Colony> colonies = new ArrayList<>(city.getColonies());
        Collections.sort(colonies);
        city.setColonies(colonies);
    }

    private DtoInDemographicExcelFile reedCells(final Iterator<Cell> cellsInRow) {
        final DtoInDemographicExcelFile file = new DtoInDemographicExcelFile();
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
            final Cell currentCell = cellsInRow.next();
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
        return file;
    }

    private void setStateFromSetStates(final Set<State> listSetStates, final String nameState, final String nameCity,
            final String nameColony, final String codePostal) {
        State state = listSetStates.stream()
                .filter(s -> s!=null && s.getName() !=null)
                .filter(s -> s.getName().equals(nameState)).findFirst().orElse(null);

        if (state == null) {
            state = new State();
            state.setName(nameState);
            state.setCities(new ArrayList<>());
            listSetStates.add(state);
        }
        final City city = setCity(state, nameCity);
        city.getColonies().add(setColony(nameColony, codePostal));
    }

    private City setCity(final State state, final String nameCity) {
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

    private Colony setColony(final String nameColony, final String codePostal) {
        final Colony colony = new Colony();
        colony.setName(nameColony);
        colony.setPostalCode(codePostal);
        return colony;
    }

    @Override
    public List<String> uploadFiles(final MultipartFile[] files) {
        return Arrays.stream(files).map(file -> {
            try {
                log.debug(LOG_FILE, file.getName());
                return copy(file);
            } catch (IOException e) {
                log.error(e.getMessage());
                return null;
            }
        }).toList();
    }

    @Override
    public ResponseData<DtoInProduct> uploadProducts(final MultipartFile file) {
        final long timeStart = new Date().getTime();
        Utils.hasExcelFormat(file, messageSource);
        try {
            final List<DtoInProduct> listProducts = excelToListProducts(file.getInputStream());
            final long timeUpload = new Date().getTime();
            final String strTimeUpload = getFinishTimeStr(timeStart, timeUpload);
            if (listProducts != null && !listProducts.isEmpty()) {
                log.debug(LOG_PRODUCTS_LOADED, listProducts.size());
            }
            log.debug(LOG_UPLOAD_TIME, strTimeUpload);
            return new ResponseData<>(listProducts, null, null, null, null);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<DtoInProduct> excelToListProducts(final InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            final Sheet sheet = workbook.getSheet(Constants.SHEET);
            final Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            final List<Product> listProducts = new ArrayList<>();
            while (rows.hasNext()) {
                final Row currentRow = rows.next();
                if (rowNumber == 0) { // Skip header
                    rowNumber++;
                    continue;
                }
                final Iterator<Cell> cellsInRow = currentRow.iterator();
                final DtoInProductExcelFile file = reedCellsProduct(cellsInRow);
                final Brand brand = getBrandFromFile(file);
                final SubBrand subBrand = getSubBrandFromFile(file);
                if (subBrand != null && brand != null) {
                    subBrand.setBrandId(brand.getId());
                    subBrandDao.save(subBrand);
                    brand.getSubBrands().add(subBrand);
                    brandDao.save(brand);
                }

                final ProductType productType = getProductTypeFromFile(file);
                final String brandId = brand == null ? null : brand.getId();
                final String subBrandId = subBrand == null ? null : subBrand.getId();
                final Product product = getProductFromFile(file, brandId, subBrandId, productType);
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
            return listProducts.isEmpty() ? null
                    : listProducts.stream().map(productMapper::productToDtoInProduct).toList();
        } catch (IOException e) {
            throw new ParseFileException(
                    Utils.getLocalMessage(messageSource, I18Constants.FAIL_PARSE_EXCEL_FILE.getKey(), Constants.SHEET));
        }
    }

    private DtoInProductExcelFile reedCellsProduct(final Iterator<Cell> cellsInRow) {
        final DtoInProductExcelFile file = new DtoInProductExcelFile();
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
            final Cell currentCell = cellsInRow.next();
            switch (cellIdx) {
                case 0 -> {
                    log.debug(LOG_GET_BRAND);
                    file.setBrand(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getBrand());
                }
                case 1 -> {
                    log.debug(LOG_GET_SUB_BRAND);
                    file.setSubBrand(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getSubBrand());
                }
                case 2 -> {
                    log.debug(LOG_GET_DESCRIPTION);
                    file.setDescription(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getDescription());
                }
                case 3 -> {
                    log.debug(LOG_DISCOUNT);
                    file.setDiscount(currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getDiscount()));
                }
                case 4 -> {
                    log.debug(LOG_GET_ENABLED);
                    file.setEnabled(currentCell.getNumericCellValue() == 1);
                    log.debug(String.valueOf(file.getEnabled()));
                }
                case 5 -> {
                    log.debug(LOG_GET_PRODUCT_NEW);
                    file.setProductNew(currentCell.getNumericCellValue() == 1);
                    log.debug(String.valueOf(file.getProductNew()));
                }
                case 6 -> {
                    log.debug(LOG_GET_PRICE);
                    file.setPrice(currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getPrice()));
                }
                case 7 -> {
                    log.debug(LOG_GET_PRODUCT_NAME);
                    file.setProductName(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getProductName());
                }
                case 8 -> {
                    log.debug(LOG_GET_STOCK);
                    file.setStock((int) currentCell.getNumericCellValue());
                    log.debug(String.valueOf(file.getStock()));
                }
                case 9 -> {
                    log.debug(LOG_GET_TITLE);
                    file.setTitle(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getTitle());
                }
                case 10 -> {
                    log.debug(LOG_GET_TYPE);
                    file.setType(Utils.removeAccents(currentCell.getStringCellValue()));
                    log.debug(file.getType());
                }
                default -> {
                }
            }
            cellIdx++;
        }
        return file;
    }

    private Brand getBrandFromFile(final DtoInProductExcelFile file) {
        if (file.getBrand() == null || file.getBrand().isEmpty()) {
            return null;
        }
        final Brand brand = brandDao.findByBrandName(file.getBrand()).orElse(createBrand(file.getBrand()));
        brand.setBrandName(file.getBrand());
        return brandDao.save(brand);
    }

    private SubBrand getSubBrandFromFile(final DtoInProductExcelFile file) {
        if (file.getSubBrand() == null || file.getSubBrand().isEmpty()) {
            return null;
        }
        final SubBrand subBrand =
                subBrandDao.findBySubBrandName(file.getSubBrand()).orElse(createSubBrand(file.getSubBrand()));
        return subBrandDao.save(subBrand);
    }

    private ProductType getProductTypeFromFile(final DtoInProductExcelFile file) {
        if (file == null || file.getType() == null || file.getType().isEmpty()) {
            return null;
        }
        final ProductType productType = productTypeDao.findProductTypeByType(file.getType())
                .orElse(createProductType(file.getType()));
        return productTypeDao.save(productType);
    }

    private Product getProductFromFile(final DtoInProductExcelFile file, final String brandId, final String subBrandId,
            final ProductType productType) {
        if (file == null || file.getProductName() == null || file.getProductName().isEmpty()) {
            return null;
        }
        final Product product = Product.builder()
                .productNew(file.getProductNew())
                .price(file.getPrice())
                .discount(file.getDiscount())
                .title(file.getTitle())
                .description(file.getDescription())
                .stock(file.getStock())
                .productType(productType)
                .brandId(brandId)
                .subBrandId(subBrandId).build();
        return productDao.save(product);
    }

    private Brand createBrand(final String brandName) {
        final Brand brand = new Brand();
        brand.setBrandName(brandName);
        brand.setSubBrands(new ArrayList<>());
        brand.setProducts(new ArrayList<>());
        return brand;
    }

    private SubBrand createSubBrand(final String subBrandName) {
        final SubBrand subBrand = new SubBrand();
        subBrand.setSubBrandName(subBrandName);
        subBrand.setProducts(new ArrayList<>());
        return subBrand;
    }

    private ProductType createProductType(final String type) {
        final ProductType productType = new ProductType();
        productType.setType(type);
        return productType;
    }

    public Path getPath(String filename) {
        return Paths.get(Constants.UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }

}
