package com.audiobea.crm.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.commons.dto.DtoInFileExcel;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"id", "cp", "colonia", "ciudad", "estado"};
    static String SHEET = "data";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<DtoInFileExcel> excelToColonies(InputStream input) {
        List<DtoInFileExcel> list = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
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
                list.add(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }

        return list;
    }
}
