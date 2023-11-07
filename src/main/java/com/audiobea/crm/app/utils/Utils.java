package com.audiobea.crm.app.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

public class Utils {
	
	private Utils() {}
	
	public static String getLocalMessage(MessageSource messageSource, String key, String... params){
        return messageSource.getMessage(key,
                params,
                Locale.ENGLISH);
    }
	
	public static String removeAccents(String txt) {
		
		if (StringUtils.isBlank(txt)) {
			return null;
		}
		txt = txt.toUpperCase();
		return txt.replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
	}
}
