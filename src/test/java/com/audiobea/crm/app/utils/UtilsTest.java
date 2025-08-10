package com.audiobea.crm.app.utils;

import com.audiobea.crm.app.core.exception.ValidFileException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UtilsTest {

    @Test
    void hasExcelFormat_fileIsNull_shouldThrowValidFileException() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(
                "Invalid Excel file format.");
        //when(messageSource.getMessage(I18Constants.NOT_VALID_EXCEL.getKey(), null, Locale.ENGLISH))
        //        .thenReturn("Invalid Excel file format.");

        Exception exception = assertThrows(ValidFileException.class, () ->
                Utils.hasExcelFormat(null, messageSource));
        System.out.println(exception.getMessage());

        assertEquals("Invalid Excel file format.", exception.getMessage());
        verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void hasExcelFormat_fileContentTypeIsBlank_shouldThrowValidFileException() {
        MultipartFile file = mock(MultipartFile.class);
        MessageSource messageSource = mock(MessageSource.class);

        when(file.getContentType()).thenReturn(" ");
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Invalid Excel file format.");

        Exception exception = assertThrows(ValidFileException.class, () ->
                Utils.hasExcelFormat(file, messageSource));

        assertEquals("Invalid Excel file format.", exception.getMessage());
        verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void hasExcelFormat_fileContentTypeIsNotExcel_shouldThrowValidFileException() {
        MultipartFile file = mock(MultipartFile.class);
        MessageSource messageSource = mock(MessageSource.class);

        when(file.getContentType()).thenReturn("application/pdf");
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Invalid Excel file format.");

        Exception exception = assertThrows(ValidFileException.class, () ->
                Utils.hasExcelFormat(file, messageSource));

        assertEquals("Invalid Excel file format.", exception.getMessage());
        verify(messageSource).getMessage(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void hasExcelFormat_fileIsValidExcelFormat_shouldNotThrowException() {
        MultipartFile file = mock(MultipartFile.class);
        MessageSource messageSource = mock(MessageSource.class);

        when(file.getContentType()).thenReturn(Constants.TYPE);

        assertDoesNotThrow(() -> Utils.hasExcelFormat(file, messageSource));
        verify(file, times(2)).getContentType();
        verifyNoInteractions(messageSource);
    }
}