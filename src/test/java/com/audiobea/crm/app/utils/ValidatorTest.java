package com.audiobea.crm.app.utils;

import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.core.exception.NoSuchElementsFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ValidatorTest {

    /**
     * Unit tests for the validatePage method in the Validator class.
     * <p>
     * The validatePage method checks if a given Page object is null or empty,
     * throwing a NoSuchElementsFoundException if the criteria are not met.
     * Otherwise, the method does not throw any exception.
     */

    @Test
    void validatePage_throwsNoSuchElementsFoundException_whenPageIsNull() {
        // Arrange
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        when(messageSource.getMessage(Mockito.eq(I18Constants.NO_ITEMS_FOUND.getKey()), Mockito.any(), Mockito.any()))
                .thenReturn("No items found");

        // Act & Assert
        assertThrows(NoSuchElementsFoundException.class, () -> Validator.validatePage(null, messageSource));
    }

    @Test
    void validatePage_throwsNoSuchElementsFoundException_whenPageIsEmpty() {
        // Arrange
        Page<?> pageable = new PageImpl<>(Collections.emptyList());
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        when(messageSource.getMessage(Mockito.eq(I18Constants.NO_ITEMS_FOUND.getKey()), Mockito.any(), Mockito.any()))
                .thenReturn("No items found");

        // Act & Assert
        assertThrows(NoSuchElementsFoundException.class, () -> Validator.validatePage(pageable, messageSource));
    }

    @Test
    void validatePage_doesNotThrowException_whenPageIsNotEmpty() {
        // Arrange
        Page<?> pageable = new PageImpl<>(Collections.singletonList("Test Item"));
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        // Act & Assert
        Validator.validatePage(pageable, messageSource);
    }
}