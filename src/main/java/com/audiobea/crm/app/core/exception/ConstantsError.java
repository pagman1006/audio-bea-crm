package com.audiobea.crm.app.core.exception;

public class ConstantsError {

    private ConstantsError() {  }

    public static final String GLOBAL_EXCEPTION_HANDLER = "GLOBAL_EXCEPTION_HANDLER";
    public static final String UNKNOWN_MESSAGE_ERROR = "Unknown error occurred";
    public static final String VALIDATION_ERROR = "Validation error. Check 'errors' field for details.";
    public static final String TRUE = "true";
    public static final String FAILED_FIND_ELEMENT_ERROR = "Failed to find the requested element {}";
    public static final String FAILED_FIND_ELEMENTS_ERROR = "Failed to find the requested elements {}";
    public static final String INVALID_EXCEL_FILE_ERROR = "Is not a valid excel file! {}";
    public static final String UPLOAD_FILE_ERROR = "Failed to upload file, {}";
    public static final String PARSE_FILE_EXCEL_ERROR = "Failed to parse excel file, {}";
    public static final String FIND_FILE_ERROR = "Failed to find file, {}";
    public static final String AUTHORIZATION_ERROR = "Failed authorization, {}";
    public static final String ACCESS_DENIED_ERROR = "Access denied, {}";
    public static final String FORBIDDEN_ERROR = "You do not have privileges to consult this information: {}";
    public static final String DUPLICATE_ERROR = "Duplicate Entry: {}";
    public static final String LOGIN_ERROR_NOT_EXIST = "Login Error: username {} not exist!";
    public static final String LOGIN_ERROR_ROLES = "Login Error: User '{} don't have roles!";
}
