package com.audiobea.crm.app.business.dao.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "sub_brands")
public class SubBrand implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sub_brand_id")
	private Long id;
	
	@Size(min = 3, max = 60)
	@Column(length = 60)
	private String subBrandName;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	private boolean enabled;

}
