package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInPhone;
import com.audiobea.crm.app.commons.dto.EnumPhoneType;
import com.audiobea.crm.app.dao.customer.model.Phone;
import com.audiobea.crm.app.dao.customer.model.PhoneType;
import com.audiobea.crm.app.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = Constants.SPRING)
public interface PhoneMapper {

	@Mapping(target = "phoneType", source = "phoneType", qualifiedByName = "enumPhoneType")
	DtoInPhone phoneToDtoInPhone(Phone phone);

	@Mapping(target = "phoneType", source = "phoneType", qualifiedByName = "typePhone")
	Phone phoneDtoToPhone(DtoInPhone dtoPhone);

	@Named("enumPhoneType")
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
