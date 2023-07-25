package com.audiobea.crm.app.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class Utils {
	
	private Utils() {}
	
	public static String getLocalMessage(MessageSource messageSource, String key, String... params){
        return messageSource.getMessage(key,
                params,
                Locale.ENGLISH);
    }
}
