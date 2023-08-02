package com.audiobea.crm.app.utils;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;

import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.exception.NoSuchElementsFoundException;

public class Validator {
	
	private Validator() {}
	
    public static void validatePage(Page<?> pageable, MessageSource messageSource) {
        if (pageable == null || pageable.getContent().isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
    }

    public static void validateList(List<?> list, MessageSource messageSource) {
        if (list == null || list.isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
    }
}
