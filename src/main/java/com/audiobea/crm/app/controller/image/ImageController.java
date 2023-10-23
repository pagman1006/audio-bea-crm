package com.audiobea.crm.app.controller.image;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IImageService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInImage;
import com.audiobea.crm.app.controller.mapper.ImageMapper;
import com.audiobea.crm.app.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/image-collection")
public class ImageController {
	
	@Autowired
	private IImageService imageService;
	
	@Autowired
	private ImageMapper imageMapper;
	
	@GetMapping
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<ResponseData<DtoInImage>> getImagesCollection() {
		log.debug("image-collection start");
		List<DtoInImage> imagesDto = imageService.getImageCollection()
				.stream().map(image -> imageMapper.imageToDtoInImage(image)).collect(Collectors.toList());
		ResponseData<DtoInImage> reponse = new ResponseData<DtoInImage>(imagesDto, null, null, null, null);
		return new ResponseEntity<>(reponse, HttpStatus.OK);
	}

}
