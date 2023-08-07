package com.audiobea.crm.app.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.audiobea.crm.app.utils.Constants.TYPE;

public class ExcelHelper {
	
	private ExcelHelper() {}

    public static boolean hasExcelFormat(MultipartFile file) {
    	if (file == null || StringUtils.isBlank(file.getContentType())) {
    		return false;
    	}
        return TYPE.equals(file.getContentType());
    }
}
