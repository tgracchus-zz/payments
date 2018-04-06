package com.example.payments.v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor //see: https://github.com/rzwitserloot/lombok/issues/816
@AllArgsConstructor
public class Party {
    private String account_name;

    @NotNull
    private String account_number;

    private String account_number_code;

    private Integer account_type;

    private String address;

    @NotNull
    private String bank_id;

    @NotNull
    private String bank_id_code;

    private String name;
}
