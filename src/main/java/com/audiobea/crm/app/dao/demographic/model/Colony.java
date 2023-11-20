package com.audiobea.crm.app.dao.demographic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document("colonies")
public class Colony implements Serializable, Comparable<Colony> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private String postalCode;

    @DocumentReference
    private State state;

    @DocumentReference
    private City city;

    @Override
    public int compareTo(Colony c) {
        return name.compareTo(c.getName());
    }

}
