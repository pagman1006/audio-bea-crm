package com.audiobea.crm.app.controller.upload;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/audio-bea/uploads/excel")
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/colonies")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInFileResponse> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file) {
        log.debug("Load started of file");
        DtoInFileResponse response = uploadService.uploadExcelFile(file);
        log.debug("Load finished of file");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
