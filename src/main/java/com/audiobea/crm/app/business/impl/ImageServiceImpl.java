package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.IImageService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInImage;
import com.audiobea.crm.app.commons.mapper.ImageMapper;
import com.audiobea.crm.app.dao.product.IImageDao;
import com.audiobea.crm.app.dao.product.model.Image;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ImageServiceImpl implements IImageService {

    private MessageSource messageSource;
    private IImageDao imageDao;
    private ImageMapper imageMapper;

    @Override
    public ResponseData<DtoInImage> getImageCollection() {
        List<Image> images = imageDao.findAll();
        Validator.validateList(images, messageSource);
        log.debug("Image-collection size: {}", images.size());
        return new ResponseData<>(images.stream().map(img -> imageMapper.imageToDtoInImage(img))
                .collect(Collectors.toList()), null, null, null, null);
    }

    @Override
    public DtoInImage addImageCollection(DtoInImage image) {
        return imageMapper.imageToDtoInImage(imageDao.save(imageMapper.imageDtoInToImage(image)));
    }

}
