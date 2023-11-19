package com.audiobea.crm.app.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DtoInEmail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    @NotEmpty
    @Email
    private String email;
    private EnumEmailType emailType;

    private boolean enabled;

}
