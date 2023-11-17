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
public class DtoInInvoice implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date purchaseDate;

	private List<DtoInCashOrder> cashOrders;

}
