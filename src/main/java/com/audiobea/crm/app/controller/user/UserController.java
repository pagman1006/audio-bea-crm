package com.audiobea.crm.app.controller.user;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInUser>> getUsers(
            @RequestParam(name = "username", defaultValue = "", required = false) String username,
            @RequestParam(name = "role", defaultValue = "", required = false) String role,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return new ResponseEntity<>(userService.getUsers(username, role, page, pageSize), HttpStatus.OK);
    }

    @PostMapping
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInUser> addUser(@RequestBody DtoInUser requestUser) {
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        return new ResponseEntity<>(userService.saveUser(requestUser), HttpStatus.CREATED);
    }

    // TODO: Implementar validacion del mismo usuario
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @Produces({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DtoInUser> getUser(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DtoInUser> updateUser(@PathVariable(value = "id") Long id, @RequestBody DtoInUser user) {
        return new ResponseEntity<>(userService.updateUser(id, (user)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> deleteUserById(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
