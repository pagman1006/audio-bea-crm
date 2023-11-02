package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInRole;
import com.audiobea.crm.app.dao.user.model.Role;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface RoleMapper {

    DtoInRole roleToDtoInRole(Role role);

    Role roleDtoInToRole(DtoInRole dtoRole);

}
