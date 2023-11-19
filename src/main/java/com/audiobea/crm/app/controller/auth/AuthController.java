package com.audiobea.crm.app.controller.auth;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(Constants.URL_BASE)
public class AuthController {

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInUser> login(@RequestBody DtoInUser requestUser) {
		log.debug("login");
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/refreshToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DtoInUser> refreshToken(Principal principal) {
		log.debug("refreshToken");
		return ResponseEntity.ok().build();
	}

}
