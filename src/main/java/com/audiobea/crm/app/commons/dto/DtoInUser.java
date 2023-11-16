package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;
    private String password;
    private boolean enabled;

    private List<DtoInRole> roles;
}
