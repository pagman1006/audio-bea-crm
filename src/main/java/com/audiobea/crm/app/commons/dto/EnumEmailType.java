package com.audiobea.crm.app.commons.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@Getter
@XmlRootElement(name = "enumEmailType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlType(name = "enumEmailType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlAccessorType(XmlAccessType.FIELD)
public enum EnumEmailType{
	@JsonProperty("PERSONAL")
	PERSONAL("PERSONAL"),
	@JsonProperty("OFFICE")
	OFFICE("OFFICE"),
	@JsonProperty("OTHER")
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
