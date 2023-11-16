package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInCustomer implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String lastName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private boolean enabled;
	
	private DtoInUser user;
	private List<DtoInAddress> address;
	private List<DtoInPhone> phones;
	private List<DtoInEmail> emails;
	private List<DtoInInvoice> invoices;

}
