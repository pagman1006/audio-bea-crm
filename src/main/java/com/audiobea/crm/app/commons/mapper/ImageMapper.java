package com.audiobea.crm.app.commons.mapper;

import org.mapstruct.Mapper;

import com.audiobea.crm.app.commons.dto.DtoInImage;
import com.audiobea.crm.app.dao.product.model.Image;
import com.audiobea.crm.app.utils.Constants;

@Mapper(componentModel = Constants.SPRING)
public interface ImageMapper {
	
	DtoInImage imageToDtoInImage(Image image);
	
	Image imageDtoInToImage(DtoInImage image);

}
