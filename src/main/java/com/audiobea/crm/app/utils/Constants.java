package com.audiobea.crm.app.utils;

public class Constants {
	private Constants() { }

	public static final String SPRING = "spring";

    public static final String TOKEN = "token";
    public static final long EXPIRATION_DATE = 604800000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String USER = "user";
    public static final String AUTHORITIES = "authorities";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_BODY = "Hi %s, you have logged in successfully!";
    public static final String MESSAGE_BODY_ERROR = "Authentication error: user or password wrong!";
    public static final String ERROR = "error";

	public static final String XS_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String TITLE_WADL = "API REST AUDIO-BEA WADL";
	
	public static final String UPLOADS_FOLDER = "uploads";
	public static final String SHEET = "data";
	public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final String FIRST_NAME = "firstName";
	public static final String SECOND_NAME = "secondName";
	public static final String FIRST_LAST_NAME = "firstLastName";
	public static final String SECOND_LAST_NAME = "secondLastName";

	public static final String BRAND = "brandName";
	public static final String SUB_BRAND = "subBrandName";
	public static final String SUB_BRAND_BRAND_NAME = "brandBrandName";

	public static final String CITY_NAME = "name";

	public static final String ALL = "All";

	// Queries
	public static final String FIND_CITIES_BY_STATE_ID = "SELECT * FROM cities WHERE state_id = :stateId";
	public static final String FIND_CITIES_BY_STATE_NAME_CITY_NAME = "SELECT * FROM cities WHERE state_id IN (SELECT id FROM states WHERE name LIKE %:state%) AND name LIKE %:city%";

	public static final String FIND_COLONIES = "SELECT * FROM colonies WHERE name LIKE %:colony% AND postal_code LIKE %:postalCode%";
	public static final String FIND_COLONIES_BY_CITY_ID = "SELECT * FROM colonies WHERE city_id = :cityId AND name LIKE %:colony% AND postal_code LIKE %:postalCode%";
	public static final String FIND_COLONIES_BY_STATE_ID = "SELECT * FROM colonies WHERE city_id IN (SELECT city_id FROM cities where state_id = :stateId) AND name LIKE %:colony% AND postal_code like %:postalCode%";
//    public static final String FIND_COLONIES_BY_STATE_ID_CITY_ID = "SELECT * FROM colonies WHERE colonies.city_id IN (SELECT DISTINCT ci.city_id FROM cities ci WHERE ci.state_id = :stateId AND ci.city_id = :cityId)";
	public static final String FIND_COLONIES_BY_STATE_ID_CITY_ID_CODE_POSTAL = "SELECT * FROM colonies WHERE colonies.city_id IN (SELECT DISTINCT ci.city_id FROM cities ci WHERE ci.state_id = :stateId AND ci.city_id = :cityId) AND colonies.name LIKE %:colony% AND colonies.postal_code LIKE %:postalCode%";
	public static final String FIND_COLONIES_BY_STATE_NAME_CITY_NAME_COLONY_NAME_CODE_POSTAL = "SELECT * FROM colonies WHERE city_id IN (SELECT city_id FROM cities WHERE state_id IN (SELECT id FROM states WHERE name LIKE %:state%) AND name like %:city%) AND name LIKE %:colony% AND postal_code LIKE %:postalCode%";

	public static final String FIND_PRODUCTS_BY_NEW_PRODUCT_BRAND_SUB_BRAND = "SELECT * FROM products WHERE product_name LIKE %:productName% AND sub_brand_id IN (SELECT sub_brand_id FROM sub_brands WHERE sub_brand_name LIKE %:subBrandName% AND brand_id IN (SELECT brand_id FROM brands WHERE brand_name LIKE %:brandName%)) AND product_type_id IN (SELECT product_type_id FROM products_type WHERE type LIKE %:productType%) AND new_product = 1";
	public static final String FIND_PRODUCTS_BY_BRAND_SUB_BRAND = "SELECT * FROM products WHERE product_name LIKE %:productName% AND sub_brand_id IN (SELECT sub_brand_id FROM sub_brands WHERE sub_brand_name LIKE %:subBrandName% AND brand_id IN (SELECT brand_id FROM brands WHERE brand_name LIKE %:brandName%)) AND product_type_id IN (SELECT product_type_id FROM products_type WHERE type LIKE %:productType%)";

	public static final String FIND_USERS_BY_USERNAME_ROLES = "SELECT u.* FROM users u LEFT JOIN authorities a ON u.user_id = a.user_id WHERE (u.username like %:username%) AND (a.authority like %:role%)";

}
