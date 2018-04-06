package com.example.payments.v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor //see: https://github.com/rzwitserloot/lombok/issues/816
@AllArgsConstructor
public class PaymentAttributes {
    @NotNull
    private String amount;

    @NotNull
    private Party beneficiary_party;

    @NotNull
    private ChargesInformation charges_information;

    @NotNull
    private String currency;

    @NotNull
    private Party debtor_party;

    @NotNull
    private String end_to_end_reference;

    private Fx fx;

    @NotNull
    private String numeric_reference;

    @NotNull
    private String payment_id;

    private String payment_purpose;

    @NotNull
    private String payment_scheme;

    @NotNull
    private String payment_type;

    @NotNull
    private String processing_date;
    private String reference;
    private String scheme_payment_sub_type;
    private String scheme_payment_type;


    @NotNull
    private Party sponsor_party;


}
