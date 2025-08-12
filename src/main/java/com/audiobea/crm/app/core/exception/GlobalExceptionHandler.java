package com.audiobea.crm.app.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;

import static com.audiobea.crm.app.core.exception.ConstantsError.*;

@Slf4j(topic = GLOBAL_EXCEPTION_HANDLER)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), VALIDATION_ERROR);

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    @Override
    public ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @Nullable Object body,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        return buildErrorResponse(ex, status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatusCode httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatusCode httpStatus,
            WebRequest request) {
        final ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value) && value.length > 0 && value[0].contentEquals(TRUE);
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementFoundException itemNotFoundException,
            WebRequest request) {
        log.error(FAILED_FIND_ELEMENT_ERROR, itemNotFoundException.getMessage());
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NoSuchElementsFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementsFoundException(
            NoSuchElementsFoundException itemsNotFoundException, WebRequest request) {
        log.error(FAILED_FIND_ELEMENTS_ERROR, itemsNotFoundException.getMessage());
        return buildErrorResponse(itemsNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ValidFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidFileException(ValidFileException fileNotValidException,
            WebRequest request) {
        log.error(INVALID_EXCEL_FILE_ERROR, fileNotValidException.getMessage());
        return buildErrorResponse(fileNotValidException, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UploadFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleUploadFileException(UploadFileException uploadFileException,
            WebRequest request) {
        log.error(UPLOAD_FILE_ERROR, uploadFileException.getMessage());
        return buildErrorResponse(uploadFileException, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ParseFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleParseFileException(ParseFileException parseFileException, WebRequest request) {
        log.error(PARSE_FILE_EXCEL_ERROR, parseFileException.getMessage());
        return buildErrorResponse(parseFileException, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NoSuchFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleNoSuchFileException(NoSuchFileException noSuchFileException,
            WebRequest request) {
        log.error(FIND_FILE_ERROR, noSuchFileException.getMessage());
        return buildErrorResponse(noSuchFileException, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAuthenticationFailedException(AuthenticationException authenticationException,
            WebRequest request) {
        log.error(AUTHORIZATION_ERROR, authenticationException.getMessage());
        return buildErrorResponse(authenticationException, "Failed authorization", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException,
            WebRequest request) {
        log.error(ACCESS_DENIED_ERROR, accessDeniedException.getMessage());
        return buildErrorResponse(accessDeniedException, "AccessDenied ", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException forbiddenException, WebRequest request) {
        log.error(FORBIDDEN_ERROR, forbiddenException.getMessage());
        return buildErrorResponse(forbiddenException, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleConstraintViolationException(DuplicateRecordException exception,
            WebRequest request) {
        log.error(DUPLICATE_ERROR, exception.getMessage());
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error(UNKNOWN_MESSAGE_ERROR, exception);
        return buildErrorResponse(exception, UNKNOWN_MESSAGE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
