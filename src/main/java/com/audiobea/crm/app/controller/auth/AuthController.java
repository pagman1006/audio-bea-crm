package com.audiobea.crm.app.controller.auth;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(Constants.URL_BASE)
public class AuthController {

    @PostMapping("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInUser> login(@RequestBody DtoInUser requestUser) {
        log.debug("login");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refreshToken")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInUser> refreshToken(Principal principal) {
        log.debug("refreshToken");
        return ResponseEntity.ok().build();
    }

}
