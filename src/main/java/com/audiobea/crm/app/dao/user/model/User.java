package com.audiobea.crm.app.dao.user.model;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Document("users")
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String username;

	@Size(min = 60, max = 60)
	private String password;
	private boolean enabled;

	@DocumentReference
	private List<Role> roles;

}
