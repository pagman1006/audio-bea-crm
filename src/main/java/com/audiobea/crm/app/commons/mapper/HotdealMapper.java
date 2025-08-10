package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInHotDeal;
import com.audiobea.crm.app.dao.product.model.HotDeal;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface HotDealMapper {
	
	DtoInHotDeal hotdealToDtoInHotdeal(HotDeal hotDeal);
	
	HotDeal hotdealDtoInToHotdeal(DtoInHotDeal hotDeal);

}
