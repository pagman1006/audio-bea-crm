package com.audiobea.crm.app.controller.mapper;

import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.audiobea.crm.app.commons.dto.DtoInEmail;
import com.audiobea.crm.app.commons.dto.EnumEmailType;
import com.audiobea.crm.app.dao.customer.model.Email;
import com.audiobea.crm.app.dao.customer.model.EmailType;

@Mapper(componentModel = "spring")
public interface EmailMapper {

	@Mapping(target = "emailType", source="emailType", qualifiedByName = "enumTypeEmail")
	DtoInEmail phoneToDtoInPhone(Email email);
	
	@Named("enumTypeEmail")
	default EnumEmailType mapEnumtype(EmailType emailType) {
		if (emailType == null || StringUtils.isBlank(emailType.getType())) {
			return null;
		}
		return EnumEmailType.valueOf(emailType.getType());
	}
}
