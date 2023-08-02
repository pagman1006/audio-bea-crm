package com.audiobea.crm.app.controller.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.audiobea.crm.app.controller.mapper.ListUserMapper;
import com.audiobea.crm.app.controller.mapper.UserMapper;
import com.audiobea.crm.app.core.DatoAuditable;
import com.audiobea.crm.app.dao.user.model.User;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/users")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ListUserMapper listUserMapper;

	@Autowired
	private UserMapper userMapper;

	private final MessageSource messageSource;

	@DatoAuditable
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<ResponseData<DtoInUser>> getUsers(
			@RequestParam(name = "username", defaultValue = "", required = false) String username,
			@RequestParam(name = "role", defaultValue = "", required = false) String role,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		Page<User> pageable = userService.getUsers(username, role, page, pageSize);
		Validator.validatePage(pageable, messageSource);
		ResponseData<DtoInUser> response = new ResponseData<>(listUserMapper.usersToDtoInUsers(pageable.getContent()),
				pageable.getNumber(), pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInUser> addUser(@RequestBody DtoInUser requestUser) {
		return new ResponseEntity<>(
				userMapper.userToDtoInUser(userService.saveUser(userMapper.userDtoInToUser(requestUser))),
				HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<DtoInUser> getUser(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(userMapper.userToDtoInUser(userService.getUserById(id)), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<DtoInUser> updateUser(@PathVariable(value = "id") Long id, @RequestBody DtoInUser user) {
		return new ResponseEntity<>(
				userMapper.userToDtoInUser(userService.updateUser(id, userMapper.userDtoInToUser(user))),
				HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> deleteUserById(@PathVariable(value = "id") Long id) {
		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
