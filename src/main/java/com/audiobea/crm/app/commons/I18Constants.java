package com.audiobea.crm.app.commons;

import lombok.Getter;

@Getter
public enum I18Constants {
	
	NO_ITEM_FOUND("item.absent"),
	NO_ITEMS_FOUND("items.absent");
	
	String key;
	
	I18Constants(String key) {
		this.key = key;
	}

}
