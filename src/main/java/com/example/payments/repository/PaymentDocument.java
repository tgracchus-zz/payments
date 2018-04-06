package com.example.payments.repository;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document
@Value
@Builder(toBuilder = true)
public class PaymentDocument {
    @Id
    private UUID id;

    private String type;

    private int version;

    private UUID organizationId;
    private String amount;
    private String currency;
    private String endToEndReference;
    private String numericReference;
    private String paymentId;
    private String paymentPurpose;
    private String paymentScheme;
    private String paymentType;
    private String processingDate;
    private String reference;
    private String schemePaymentSubType;
    private String schemePaymentType;

    //Parties
    private Account beneficiaryParty;
    private Account sponsorParty;
    private Account debtorParty;

    //FX
    private String contractReference;
    private String exchangeRate;
    private String originalAmount;
    private String originalCurrency;

    //Chargers
    private String bearerCode;
    private List<SenderCharges> senderCharges;
    private String receiverChargesAmount;
    private String receiverChargesCurrency;


    @Value
    @Builder
    public static class Account {
        private String account_name;
        private String account_number;
        private String account_number_code;
        private Integer account_type;
        private String address;
        private String bank_id;
        private String bank_id_code;
        private String name;
    }


    @Value
    @Builder
    public static class SenderCharges {
        String amount;
        String currency;
    }

}
