package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DtoInCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String secondName;
	private String firstLastName;
	private String secondLastName;
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
