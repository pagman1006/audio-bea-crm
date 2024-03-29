package com.audiobea.crm.app.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface UserMapper {

	@Mapping(target = "password", source = "password", qualifiedByName = "passwordMap")
	DtoInUser userToDtoInUser(User user);

	User userDtoInToUser(DtoInUser dtoUser);

	@Named("passwordMap")
	default String passwordMap(String password) {
		return "**********";
	}

}
