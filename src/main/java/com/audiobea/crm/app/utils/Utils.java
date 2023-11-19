package com.audiobea.crm.app.utils;

import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.core.exception.ValidFileException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

import static com.audiobea.crm.app.utils.Constants.TYPE;

public class Utils {
	
	private Utils() {}

	public static void hasExcelFormat(MultipartFile file, MessageSource messageSource) {
		if (file == null || StringUtils.isBlank(file.getContentType()) || !TYPE.equals(file.getContentType())) {
			throw new ValidFileException(Utils.getLocalMessage(messageSource, I18Constants.NOT_VALID_EXCEL.getKey()));
		}
	}
	
	public static String getLocalMessage(MessageSource messageSource, String key, String... params){
        return messageSource.getMessage(key,
                params,
                Locale.ENGLISH);
    }
	
	public static String removeAccents(String txt) {
		if (StringUtils.isBlank(txt)) {
			return null;
		}
		return txt
				.toUpperCase().replace("Á", "A").replace("É", "E")
				.replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
	}
}
