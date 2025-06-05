package com.audiobea.crm.app.dao.demographic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String stateId;
    private String cityId;

    @Override
    public int compareTo(Colony c) {
        return name.compareTo(c.getName());
    }

}
