package com.audiobea.crm.app.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.IUserService;
import com.audiobea.crm.app.dao.user.model.User;

@RestController
@RequestMapping("/usuarios")
public class UserController {
	
	@Autowired
	private IUserService usuarioService;
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<User> getUsers() {
		return usuarioService.getUsers();
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public String addUser(@RequestBody User user) {
		String respuesta = usuarioService.saveUser(user)? 
				"Usuario Insertado" : "Error, existe ese usuario";
		return respuesta;
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public String updateUser(@PathVariable(value = "id") Long id, @RequestBody User user) {
		System.out.println("Id: " + id);
		String respuesta = usuarioService.updateUser(id, user)?
				"Usuario actualizado": "Error al actualizar usuario";
		return respuesta;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public String deleteUserById(@PathVariable(value = "id") Long id) {
		String respuesta = usuarioService.deleteUserById(id)? 
				"Se eliminó correctamente": "Error, ocurrió un error al eliminar el registro";
		return respuesta;
	}

}
