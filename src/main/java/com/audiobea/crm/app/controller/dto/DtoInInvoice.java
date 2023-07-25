package com.audiobea.crm.app.controller.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DtoInInvoice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date purchaseDate;

	private List<DtoInCashOrder> cashOrders;

}
