package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = Constants.SPRING)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    DtoInUser userToDtoInUser(User user);

    User userDtoInToUser(DtoInUser dtoUser);

}
