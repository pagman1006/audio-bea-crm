package com.audiobea.crm.app.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.utils.Constants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<DtoInUser>> getUsers(
            @RequestParam(name = "username", defaultValue = "", required = false) String username,
            @RequestParam(name = "role", defaultValue = "", required = false) String role,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.debug("getUsers");
        return new ResponseEntity<>(userService.getUsers(username, role, page, pageSize), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DtoInUser> addUser(@RequestBody DtoInUser requestUser) {
        log.debug("addUser");
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        return new ResponseEntity<>(userService.saveUser(requestUser), HttpStatus.CREATED);
    }

    // TODO: Implement validation of the same user
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DtoInUser> getUser(@PathVariable(value = "id") Long id) {
        log.debug("getUser");
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DtoInUser> updateUser(@PathVariable(value = "id") Long id, @RequestBody DtoInUser user) {
        log.debug("updateUser");
        return new ResponseEntity<>(userService.updateUser(id, (user)), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteUserById(@PathVariable(value = "id") Long id) {
        log.debug("deleteUserById");
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
