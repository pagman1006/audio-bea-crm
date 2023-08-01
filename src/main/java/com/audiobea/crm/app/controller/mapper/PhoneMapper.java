package com.audiobea.crm.app.controller.mapper;

import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.audiobea.crm.app.commons.dto.DtoInPhone;
import com.audiobea.crm.app.commons.dto.EnumPhoneType;
import com.audiobea.crm.app.dao.customer.model.Phone;
import com.audiobea.crm.app.dao.customer.model.PhoneType;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface PhoneMapper {

	@Mapping(target = "phoneType", source="phoneType", qualifiedByName = "enumTypePhone")
	DtoInPhone phoneToDtoInPhone(Phone phone);
	
	@Mapping(target = "phoneType", source="phoneType", qualifiedByName = "typePhone")
	Phone phoneDtoToPhone(DtoInPhone dtoPhone);
	
	@Named("enumTypePhone")
	default EnumPhoneType mapEnumType(PhoneType phoneType) {
		if (phoneType == null || StringUtils.isBlank(phoneType.getType())) {
			return null;
		}
		return EnumPhoneType.valueOf(phoneType.getType());
	}
	
	@Named("typePhone")
	default PhoneType mapPhone(EnumPhoneType type) {
		if (type == null) {
			return null;
		}
		PhoneType phoneType = new PhoneType();
		phoneType.setType(type.name());
		return phoneType;
	}
}
