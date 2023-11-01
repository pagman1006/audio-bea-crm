package com.audiobea.crm.app.controller.auth;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

@RestController
@RequestMapping(Constants.URL_BASE)
public class AuthController {

    @PostMapping("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInUser> login(@RequestBody DtoInUser requestUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refreshToken")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInUser> refreshToken(Principal principal) {
        return ResponseEntity.ok().build();
    }

}
