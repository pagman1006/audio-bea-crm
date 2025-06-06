package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInEmail;
import com.audiobea.crm.app.commons.dto.EnumEmailType;
import com.audiobea.crm.app.dao.customer.model.Email;
import com.audiobea.crm.app.dao.customer.model.EmailType;
import com.audiobea.crm.app.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = Constants.SPRING)
public interface EmailMapper {

	@Mapping(target = "emailType", source = "emailType", qualifiedByName = "enumTypeEmail")
	@Mapping(target = "email", source = "emailAddress")
	DtoInEmail emailToDtoInEmail(Email email);

	@Mapping(target = "emailType", source = "emailType", qualifiedByName = "typeEmail")
	@Mapping(target = "emailAddress", source = "email")
	Email emailDtoInToEmail(DtoInEmail dtoEmail);

	@Named("enumTypeEmail")
	default EnumEmailType mapEnumType(EmailType emailType) {
		if (emailType == null || StringUtils.isBlank(emailType.getType())) {
			return null;
		}
		return EnumEmailType.valueOf(emailType.getType());
	}

	@Named("typeEmail")
	default EmailType mapEmail(EnumEmailType type) {
		if (type == null) {
			return null;
		}
		EmailType emailType = new EmailType();
		emailType.setType(type.name());
		return emailType;
	}
}
