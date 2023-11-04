package com.audiobea.crm.app.controller.image;

import com.audiobea.crm.app.business.IImageService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInImage;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/image-collection")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @GetMapping
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInImage>> getImagesCollection() {
        log.debug("image-collection start");
        return new ResponseEntity<>(imageService.getImageCollection(), HttpStatus.OK);
    }

}
