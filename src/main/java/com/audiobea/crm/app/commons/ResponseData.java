package com.audiobea.crm.app.commons;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import lombok.Data;

@Data
public class ResponseData<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Transient
	private List<T> data;
	
	private Integer page;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	
	public ResponseData(List<T> data, Integer page, Integer pageSize, Long totalElements, Integer totalPages) {
		super();
		this.data = data;
		this.page = page;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public ResponseData() {
		super();
	}

}
