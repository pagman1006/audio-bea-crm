package com.audiobea.crm.app.utils;

public class ConstantsController {

    private ConstantsController() { }

    public static final String BASE_PATH = "/audio-bea/v1/api/";

    public static final String LOGIN_PATH = "/login";
    public static final String REFRESH_TOKEN_PATH = "${path.refresh-token}";

    public static final String ADMIN_CUSTOMER_PATH = "${path.admin.customers}";
    public static final String DEMOGRAPHIC_PATH = "${path.demographics}";

    public static final String STATES_PATH = "${path.states}";
    public static final String STATES_ID_CITIES_PATH = "${path.state-id.cities}";
    public static final String STATES_ID_CITY_ID_COLONIES_PATH = "${path.state-id.city-id.colonies}";
    public static final String COLONIES_PATH = "${path.colonies}";


    public static final String IMAGE_COLLECTION_PATH = "${path.image.collection}";

    public static final String PRODUCTS_PATH = "${path.products}";

    public static final String BRANDS_PATH = "${path.brands}";
    public static final String BRAND_ID_SUB_BRANDS_PATH = "${path.brand-id.sub-brands}";
    public static final String SUB_BRAND_ID_SUB_BRAND_ID_PATH = "${path.brand-id.sub-brand-id}";


    public static final String UPLOADS_EXCEL_PATH = "${path.uploads.excel}";
    public static final String UPLOADS_COLONIES_PATH = "${path.uploads.colonies}";
    public static final String UPLOADS_PRODUCTS_PATH = "${path.uploads.products}";

    public static final String USERS_PATH = "${path.users}";

}
