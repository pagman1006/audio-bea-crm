package com.audiobea.crm.app.dao.demographic.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "colonies")
public class Colony implements Serializable, Comparable<Colony> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colony_id")
    private Long id;

    @Column(length = 60)
    private String name;

    @Column(length = 5)
    private String postalCode;

    @Override
    public int compareTo(Colony c) {
        return name.compareTo(c.getName());
    }

}
