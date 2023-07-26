package com.audiobea.crm.app.controller.mapper;

import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.audiobea.crm.app.commons.dto.DtoInPhone;
import com.audiobea.crm.app.commons.dto.EnumPhoneType;
import com.audiobea.crm.app.dao.customer.model.Phone;
import com.audiobea.crm.app.dao.customer.model.PhoneType;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

	@Mapping(target = "phoneType", source="phoneType", qualifiedByName = "enumTypePhone")
	DtoInPhone phoneToDtoInPhone(Phone phone);
	
	@Named("enumTypePhone")
	default EnumPhoneType mapEnumtype(PhoneType phoneType) {
		if (phoneType == null || StringUtils.isBlank(phoneType.getType())) {
			return null;
		}
		return EnumPhoneType.valueOf(phoneType.getType());
	}
}
