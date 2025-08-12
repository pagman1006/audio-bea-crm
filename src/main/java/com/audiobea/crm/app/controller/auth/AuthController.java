package com.audiobea.crm.app.controller.auth;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.audiobea.crm.app.utils.ConstantsController.LOGIN_PATH;
import static com.audiobea.crm.app.utils.ConstantsController.REFRESH_TOKEN_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_LOGIN;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_REFRESH_TOKEN;

@Slf4j
@RestController
@RequestMapping
public class AuthController {

	@PostMapping(path = LOGIN_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInUser> login(@RequestBody DtoInUser requestUser) {
		log.debug(LOG_LOGIN);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = REFRESH_TOKEN_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInUser> refreshToken(Principal principal) {
		log.debug(LOG_REFRESH_TOKEN);
		return ResponseEntity.ok().build();
	}

}
