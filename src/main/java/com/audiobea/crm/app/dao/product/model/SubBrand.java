package com.audiobea.crm.app.dao.product.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	
	@Column(length = 60)
	private String subBrandName;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	private boolean enabled;

}
