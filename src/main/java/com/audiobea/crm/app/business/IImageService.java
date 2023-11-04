package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInImage;

public interface IImageService {

	ResponseData<DtoInImage> getImageCollection();
}
