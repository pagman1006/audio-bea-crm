package com.audiobea.crm.app.commons;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ResponseData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient List<T> data;

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

	public ResponseData(List<T> data, Page<?> pageable){
		super();
		this.data = data;
		this.page = pageable.getNumber();
		this.pageSize = pageable.getSize();
		this.totalElements = pageable.getTotalElements();
		this.totalPages = pageable.getTotalPages();
	}

	public ResponseData() {
		super();
	}

}
