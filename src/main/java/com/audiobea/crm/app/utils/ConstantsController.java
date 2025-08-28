package com.audiobea.crm.app.utils;

public class ConstantsController {

    private ConstantsController() { }

    public static final String LOGIN_PATH = "/login";
    public static final String REFRESH_TOKEN_PATH = "/refreshToken";
    public static final String ADMIN_CUSTOMER_PATH = "/admin/customers";
    public static final String DEMOGRAPHIC_PATH = "/demographics";
    public static final String STATES_PATH = "/states";
    public static final String COLONIES_PATH = "/colonies";

    public static final String STATES_ID_CITIES_PATH = "/states/{stateId}/cities";
    public static final String STATES_ID_CITY_ID_COLONIES_PATH = "/states/{stateId}/cities/{cityId}/colonies";

    public static final String IMAGE_COLLECTION_PATH = "/image-collection";

    public static final String BRANDS_PATH = "/brands";
    public static final String BRAND_ID_SUB_BRANDS_PATH = "/{brand-id}/sub-brands";
    public static final String SUB_BRAND_ID_SUB_BRAND_ID_PATH = "/{brand-id}/sub-brands/{sub-brand-id}";

    public static final String PRODUCTS_PATH = "/products";

    public static final String UPLOADS_EXCEL_PATH = "/uploads/excel";
    public static final String UPLOADS_COLONIES_PATH = "/colonies";
    public static final String UPLOADS_PRODUCTS_PATH = "/products";

    public static final String USERS_PATH = "/users";

    public static final String JWT_LOGIN_PATH = "/login";
    public static final String BASE_PATH = "/audio-bea/v1/api/";
}
