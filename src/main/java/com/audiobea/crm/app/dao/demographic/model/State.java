package com.audiobea.crm.app.dao.demographic.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "states")
public class State implements Serializable, Comparable<State> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
