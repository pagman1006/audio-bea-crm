package com.audiobea.crm.app.commons.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@Getter
@XmlRootElement(name = "enumPhoneType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlType(name = "enumPhoneType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlAccessorType(XmlAccessType.FIELD)
public enum EnumPhoneType {
	@JsonProperty("PERSONAL")
	PERSONAL("PERSONAL"), 
	@JsonProperty("OFFICE")
	OFFICE("OFFICE"), 
	@JsonProperty("MOBILE")
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
