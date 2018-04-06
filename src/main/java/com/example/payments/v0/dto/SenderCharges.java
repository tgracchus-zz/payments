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
public class SenderCharges {
    @NotNull
    String amount;
    @NotNull
    String currency;
}
