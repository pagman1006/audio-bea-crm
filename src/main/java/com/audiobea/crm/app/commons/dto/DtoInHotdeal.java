package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInHotdeal implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;

	@NotEmpty
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hotdeal;

}
