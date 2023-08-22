package com.audiobea.crm.app.business.dao.demographic.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import lombok.Data;

@Data
@Entity
@Table(name = "states")
public class State implements Serializable, Comparable<State> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 3, max = 60)
	@Column(length = 60)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "state_id")
	private List<City> cities;

	@Override
	public int compareTo(State s) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(s.getName())) {
			return 0;
		}
		return name.compareTo(s.getName());
	}
}
