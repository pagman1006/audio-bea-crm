package com.audiobea.crm.app.controller.upload;

import com.audiobea.crm.app.business.IUploadService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.commons.dto.DtoInProduct;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.audiobea.crm.app.utils.ConstantsController.UPLOADS_COLONIES_PATH;
import static com.audiobea.crm.app.utils.ConstantsController.UPLOADS_EXCEL_PATH;
import static com.audiobea.crm.app.utils.ConstantsController.UPLOADS_PRODUCTS_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_UPLOAD_COMPLETED_FILE;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_UPLOAD_STARTED_FILE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(UPLOADS_EXCEL_PATH)
public class UploadController {

	private final IUploadService uploadService;

    @PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = UPLOADS_COLONIES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInFileResponse> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) {
		log.debug(LOG_UPLOAD_STARTED_FILE);
		DtoInFileResponse response = uploadService.uploadExcelFile(file);
		log.debug(LOG_UPLOAD_COMPLETED_FILE);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = UPLOADS_PRODUCTS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData<DtoInProduct>> uploadProducts(@RequestParam(name = "file", required = false) MultipartFile file) {
		log.debug(LOG_UPLOAD_STARTED_FILE);
		ResponseData<DtoInProduct> response = uploadService.uploadProducts(file);
		log.debug(LOG_UPLOAD_COMPLETED_FILE);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
