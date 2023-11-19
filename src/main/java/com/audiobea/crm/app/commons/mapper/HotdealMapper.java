package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInHotdeal;
import com.audiobea.crm.app.dao.product.model.Hotdeal;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface HotdealMapper {
	
	DtoInHotdeal hotdealToDtoInHotdeal(Hotdeal hotdeal);
	
	Hotdeal hotdealDtoInToHotdeal(DtoInHotdeal hotdeal);

}
