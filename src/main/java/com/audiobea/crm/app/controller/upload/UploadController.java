package com.audiobea.crm.app.controller.upload;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads/excel")
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    @PostMapping("/colonies")
    public ResponseEntity<DtoInFileResponse> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) {
        return new ResponseEntity<>(uploadService.uploadExcelFile(file), HttpStatus.CREATED);
    }
}
