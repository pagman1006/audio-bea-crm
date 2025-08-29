package com.audiobea.crm.app.dao.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("authorities")
public class Role implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Indexed(unique = true)
	private String authority;

}
