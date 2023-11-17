package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @Email
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private boolean enabled;

    @NotNull
    private List<DtoInRole> roles;
}
