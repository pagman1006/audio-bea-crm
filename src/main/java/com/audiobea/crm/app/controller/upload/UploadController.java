package com.audiobea.crm.app.controller.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.URL_BASE + "/uploads/excel")
public class UploadController {

	@Autowired
	private IUploadService uploadService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/colonies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInFileResponse> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) {
		log.debug("Load started of file");
		DtoInFileResponse response = uploadService.uploadExcelFile(file);
		log.debug("Load finished of file");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
