package com.audiobea.crm.app.commons.dto;

import lombok.Getter;

@Getter
public enum EnumPhoneType {
	PERSONAL("PERSONAL"), 
	OFFICE("OFFICE"), 
	MOBILE("MOBILE");
	
	private final String code;
	
	EnumPhoneType(final String code) {
		this.code = code;
	}
	
	public static EnumPhoneType getEnumPhoneType(final String code) {
		for (EnumPhoneType item : EnumPhoneType.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}

}
