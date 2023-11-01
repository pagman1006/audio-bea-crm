package com.audiobea.crm.app.controller.mapper;

import java.util.List;

import com.audiobea.crm.app.commons.mapper.UserMapper;
import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING, uses = {UserMapper.class})
public interface ListUserMapper {
	
	List<DtoInUser> usersToDtoInUsers(List<User> users);
	
	List<User> usersDtoInToUsers(List<DtoInUser> dtoUsers);

}
