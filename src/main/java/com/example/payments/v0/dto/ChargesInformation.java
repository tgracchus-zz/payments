package com.example.payments.v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor //see: https://github.com/rzwitserloot/lombok/issues/816
@AllArgsConstructor
public class ChargesInformation {

    private String bearer_code;

    private List<SenderCharges> sender_charges;

    private String receiver_charges_amount;

    private String receiver_charges_currency;
}
