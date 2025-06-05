package com.audiobea.crm.app.commons.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DtoInProductRanking implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer ranking;
}
