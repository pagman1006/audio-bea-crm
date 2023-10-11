package com.audiobea.crm.app.commons;

import lombok.Getter;

@Getter
public enum I18Constants {
	
	NO_ITEM_FOUND("item.absent"),
	NO_ITEMS_FOUND("items.absent"),
	NOT_VALID_EXCEL("file.excel.not.valid"),
	UPLOAD_FILE_EXCEPTION("upload.file.exception"),
	FAIL_PARSE_EXCEL_FILE("fail.parse.excel.file"),
	NO_FILE_FOUND("file.not.found"),
	NO_PRIVILEGES("not.privileges");
	
	String key;
	
	I18Constants(String key) {
		this.key = key;
	}

}
