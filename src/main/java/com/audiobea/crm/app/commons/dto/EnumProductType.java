package com.audiobea.crm.app.commons.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@XmlRootElement(name = "enumProductType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlType(name = "enumProductType", namespace = "com:audiobea:crm:app:commons:dto")
@XmlAccessorType(XmlAccessType.FIELD)
public enum EnumProductType{
	@JsonProperty("CARSTEREO")
	CARSTEREO("CARSTEREO"),
	@JsonProperty("SUBWOOFER")
	SUBWOOFER("SUBWOOFER"),
	@JsonProperty("ALARM")
	ALARM("ALARM"),
	@JsonProperty("ACCESSORY")
	ACCESSORY("ACCESSORY");

	private final String code;

	EnumProductType(final String code) {
		this.code = code;
	}

	public static EnumProductType getEnumProductType(final String code) {
		for (EnumProductType item : EnumProductType.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
}
