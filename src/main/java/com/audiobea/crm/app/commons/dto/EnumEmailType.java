package com.audiobea.crm.app.commons.dto;

import lombok.Getter;

@Getter
public enum EnumEmailType{
	
	PERSONAL("PERSONAL"),
	OFFICE("OFFICE"),
	OTHER("OTHER");

	private final String code;

	EnumEmailType(final String code) {
		this.code = code;
	}

	public static EnumEmailType getEnumEmailType(final String code) {
		for (EnumEmailType item : EnumEmailType.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
}
