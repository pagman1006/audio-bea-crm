package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.commons.mapper.ProductMapper;
import com.audiobea.crm.app.core.exception.NoSuchFileException;
import com.audiobea.crm.app.core.exception.UploadFileException;
import com.audiobea.crm.app.dao.demographic.ICityDao;
import com.audiobea.crm.app.dao.demographic.IColonyDao;
import com.audiobea.crm.app.dao.demographic.IStateDao;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UploadServiceImplTest {

    private IStateDao stateDao;
    private ICityDao cityDao;
    private IColonyDao colonyDao;
    private IProductDao productDao;
    private IBrandDao brandDao;
    private ISubBrandDao subBrandDao;
    private IProductTypeDao productTypeDao;
    private MessageSource messageSource;
    private ProductMapper productMapper;

    private UploadServiceImpl service;

    private Path uploadsDir;

    @BeforeEach
    void setUp() throws IOException {
        stateDao = mock(IStateDao.class);
        cityDao = mock(ICityDao.class);
        colonyDao = mock(IColonyDao.class);
        productDao = mock(IProductDao.class);
        brandDao = mock(IBrandDao.class);
        subBrandDao = mock(ISubBrandDao.class);
        productTypeDao = mock(IProductTypeDao.class);
        messageSource = mock(MessageSource.class);
        productMapper = mock(ProductMapper.class);

        service = new UploadServiceImpl(stateDao, cityDao, colonyDao, productDao, brandDao, subBrandDao, productTypeDao, messageSource, productMapper);

        // Ensure uploads directory exists fresh for tests
        uploadsDir = Paths.get(Constants.UPLOADS_FOLDER).toAbsolutePath();
        if (Files.exists(uploadsDir)) {
            // Clean up any existing files from previous runs
            Files.walk(uploadsDir)
                    .map(Path::toFile)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(File::delete);
            Files.deleteIfExists(uploadsDir);
        }
        Files.createDirectory(uploadsDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(uploadsDir)) {
            Files.walk(uploadsDir)
                    .map(Path::toFile)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(File::delete);
            Files.deleteIfExists(uploadsDir);
        }
    }

    @Test
    void init_shouldCreateUploadsDirectory() throws IOException {
        // Remove directory and call init
        Files.walk(uploadsDir)
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(File::delete);
        Files.deleteIfExists(uploadsDir);

        assertFalse(Files.exists(uploadsDir));
        service.init();
        assertTrue(Files.exists(uploadsDir));
    }

    @Test
    void copy_shouldSaveFileAndReturnGeneratedName() throws IOException {
        byte[] content = "hello world".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile("file", "my.txt", "text/plain", new ByteArrayInputStream(content));

        String savedName = service.copy(file);
        assertNotNull(savedName);
        assertTrue(savedName.endsWith("_" + file.getOriginalFilename()) || savedName.contains(file.getOriginalFilename()));

        Path savedPath = uploadsDir.resolve(savedName);
        assertTrue(Files.exists(savedPath));
        assertArrayEquals(content, Files.readAllBytes(savedPath));
    }

    @Test
    void load_existingFile_shouldReturnReadableResource() throws Exception {
        // Arrange: create a file
        String name = "sample.txt";
        Path path = uploadsDir.resolve(name);
        Files.write(path, "content".getBytes(StandardCharsets.UTF_8));

        // Act
        Resource resource = service.load(name);

        // Assert
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(path.toUri(), resource.getURI());
    }

    @Test
    void load_missingFile_shouldThrowUploadFileException() throws Exception {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error loading file");
        assertThrows(UploadFileException.class, () -> service.load("not-exists.txt"));
        verify(messageSource).getMessage(anyString(), any(), any());
    }

    @Test
    void delete_existingFile_shouldDeleteWithoutError() throws Exception {
        String name = "to-delete.txt";
        Path path = uploadsDir.resolve(name);
        Files.write(path, "x".getBytes(StandardCharsets.UTF_8));
        assertTrue(Files.exists(path));

        assertDoesNotThrow(() -> service.delete(name));
        assertFalse(Files.exists(path));
    }

    @Test
    void delete_missingFile_shouldDoNothing() {
        assertDoesNotThrow(() -> service.delete("missing.txt"));
    }

    @Test
    void deleteAll_shouldRemoveUploadsDirectory() {
        // Create a file inside
        try {
            Files.write(uploadsDir.resolve("inner.txt"), "x".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fail(e);
        }
        assertTrue(Files.exists(uploadsDir));

        service.deleteAll();

        assertFalse(Files.exists(uploadsDir));
    }

    @Test
    void uploadFiles_mixedSuccessAndFailure_shouldReturnListWithNullForFailures() throws IOException {
        // File 1: ok
        MultipartFile ok = new MockMultipartFile("ok", "ok.txt", "text/plain",
                new ByteArrayInputStream("ok".getBytes(StandardCharsets.UTF_8)));
        // File 2: throws when reading
        MultipartFile bad = mock(MultipartFile.class);
        when(bad.getName()).thenReturn("bad");
        when(bad.getOriginalFilename()).thenReturn("bad.txt");
        when(bad.getInputStream()).thenThrow(new IOException("boom"));

        List<String> results = service.uploadFiles(new MultipartFile[]{ok, bad});

        assertEquals(2, results.size());
        assertNotNull(results.get(0));
        assertNull(results.get(1));
        assertTrue(Files.exists(uploadsDir.resolve(results.get(0))));
    }

    @Test
    void uploadExcelFile_shouldParseDemographicDataAndReturnCounts() throws Exception {
        // Build a simple workbook with header + 2 data rows
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(Constants.SHEET);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ignored");
        header.createCell(1).setCellValue("CP");
        header.createCell(2).setCellValue("COLONY");
        header.createCell(3).setCellValue("CITY");
        header.createCell(4).setCellValue("STATE");

        Row r1 = sheet.createRow(1);
        r1.createCell(1).setCellValue("01010");
        r1.createCell(2).setCellValue("Centro");
        r1.createCell(3).setCellValue("CDMX");
        r1.createCell(4).setCellValue("Ciudad de México");

        Row r2 = sheet.createRow(2);
        r2.createCell(1).setCellValue("02020");
        r2.createCell(2).setCellValue("Nápoles");
        r2.createCell(3).setCellValue("CDMX");
        r2.createCell(4).setCellValue("Ciudad de México");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        MultipartFile file = new MockMultipartFile("file", "demo.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(bos.toByteArray()));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("ok");

        // Mock DAOs used by async thread to just return argument
        when(stateDao.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(cityDao.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(cityDao.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
        when(colonyDao.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        DtoInFileResponse response = service.uploadExcelFile(file);
        assertNotNull(response);
        // Expect 1 state, 1 city, 2 colonies after normalization/sorting counts
        assertEquals("2", response.getStates());
        assertEquals("2", response.getCities());
        assertEquals("2", response.getColonies());
    }

    @Test
    void uploadProducts_shouldParseAndMapProducts() throws Exception {
        // Build a simple products workbook with header + 1 row
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(Constants.SHEET);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("BRAND");
        header.createCell(1).setCellValue("SUB_BRAND");
        header.createCell(2).setCellValue("DESC");
        header.createCell(3).setCellValue("DISCOUNT");
        header.createCell(4).setCellValue("ENABLED");
        header.createCell(5).setCellValue("NEW");
        header.createCell(6).setCellValue("PRICE");
        header.createCell(7).setCellValue("PRODUCT_NAME");
        header.createCell(8).setCellValue("STOCK");
        header.createCell(9).setCellValue("TITLE");
        header.createCell(10).setCellValue("TYPE");

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Acme");
        r1.createCell(1).setCellValue("Pro");
        r1.createCell(2).setCellValue("Desc");
        r1.createCell(3).setCellValue(10.0);
        r1.createCell(4).setCellValue(1.0);
        r1.createCell(5).setCellValue(1.0);
        r1.createCell(6).setCellValue(99.99);
        r1.createCell(7).setCellValue("Widget");
        r1.createCell(8).setCellValue(5.0);
        r1.createCell(9).setCellValue("Title");
        r1.createCell(10).setCellValue("TypeA");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        MultipartFile file = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(bos.toByteArray()));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("ok");

        // Mock lookups to return existing entities, and saves to echo back
        Brand brand = new Brand();
        brand.setId("B1");
        brand.setSubBrands(new java.util.ArrayList<>());
        brand.setProducts(new java.util.ArrayList<>());
        when(brandDao.findByBrandName(anyString())).thenReturn(Optional.of(brand));
        when(brandDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SubBrand subBrand = new SubBrand();
        subBrand.setId("S1");
        subBrand.setProducts(new java.util.ArrayList<>());
        when(subBrandDao.findBySubBrandName(anyString())).thenReturn(Optional.of(subBrand));
        when(subBrandDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductType productType = new ProductType();
        productType.setId("T1");
        when(productTypeDao.findProductTypeByType(anyString())).thenReturn(Optional.of(productType));
        when(productTypeDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        when(productDao.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        DtoInProduct mapped = new DtoInProduct();
        when(productMapper.productToDtoInProduct(any(Product.class))).thenReturn(mapped);

        ResponseData<DtoInProduct> resp = service.uploadProducts(file);
        assertNotNull(resp);
        assertNotNull(resp.getData());
        assertEquals(1, resp.getData().size());
        assertSame(mapped, resp.getData().get(0));
    }
}
