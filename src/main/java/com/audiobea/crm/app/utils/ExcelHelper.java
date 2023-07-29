package com.audiobea.crm.app.utils;

import org.springframework.web.multipart.MultipartFile;

import static com.audiobea.crm.app.utils.Constants.TYPE;

public class ExcelHelper {

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
}
