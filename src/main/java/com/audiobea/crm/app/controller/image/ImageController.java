package com.audiobea.crm.app.controller.image;

import com.audiobea.crm.app.business.IImageService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInImage;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.audiobea.crm.app.utils.ConstantsController.IMAGE_COLLECTION_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_IMAGE_COLLECTION_START;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(IMAGE_COLLECTION_PATH)
public class ImageController {

    private final IImageService imageService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<DtoInImage>> getImagesCollection() {
        log.debug(LOG_IMAGE_COLLECTION_START);
        return new ResponseEntity<>(imageService.getImageCollection(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInImage> addImageCollection(@RequestBody DtoInImage dtoInImage) {
        log.debug(LOG_IMAGE_COLLECTION_START);
        return new ResponseEntity<>(imageService.addImageCollection(dtoInImage), HttpStatus.OK);
    }

}
