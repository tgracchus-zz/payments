package com.example.payments.v0.dto;

import com.example.payments.repository.PaymentDocument;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor //see: https://github.com/rzwitserloot/lombok/issues/816
@AllArgsConstructor
public class Payment {

    @NotNull
    private String type;

    private UUID id;

    @NotNull
    @Min(0)
    private int version;

    @NotNull
    private UUID organisation_id;

    @NotNull
    private PaymentAttributes attributes;


    public static Payment fromPayment(PaymentDocument paymentDocument) {
        val paymentBuilderDto = com.example.payments.v0.dto.Payment.builder();
        val paymentAttributes = toPaymentAttributes(paymentDocument);
        return paymentBuilderDto
                .id(paymentDocument.getId())
                .organisation_id(paymentDocument.getOrganizationId())
                .type(paymentDocument.getType())
                .version(paymentDocument.getVersion())
                .attributes(paymentAttributes)
                .build();
    }


    private static PaymentAttributes toPaymentAttributes(PaymentDocument paymentDocument) {
        return PaymentAttributes.builder()
                .amount(paymentDocument.getAmount())
                .currency(paymentDocument.getCurrency())
                .end_to_end_reference(paymentDocument.getEndToEndReference())
                .numeric_reference(paymentDocument.getNumericReference())
                .payment_id(paymentDocument.getPaymentId())
                .payment_purpose(paymentDocument.getPaymentPurpose())
                .payment_scheme(paymentDocument.getPaymentScheme())
                .payment_type(paymentDocument.getPaymentType())
                .processing_date(paymentDocument.getProcessingDate())
                .reference(paymentDocument.getReference())
                .scheme_payment_sub_type(paymentDocument.getSchemePaymentSubType())
                .scheme_payment_type(paymentDocument.getSchemePaymentType())
                .beneficiary_party(toParty(paymentDocument.getBeneficiaryParty()))
                .sponsor_party(toParty(paymentDocument.getSponsorParty()))
                .debtor_party(toParty(paymentDocument.getDebtorParty()))
                .charges_information(toChargesInformation(paymentDocument))
                .fx(toFx(paymentDocument))
                .build();
    }

    private static Fx toFx(PaymentDocument paymentDocument) {
        return Fx.builder()
                .contract_reference(paymentDocument.getContractReference())
                .exchange_rate(paymentDocument.getExchangeRate())
                .original_amount(paymentDocument.getOriginalAmount())
                .original_currency(paymentDocument.getOriginalCurrency())
                .build();
    }

    private static Party toParty(PaymentDocument.Account account) {
        return Party.builder()
                .account_name(account.getAccount_name())
                .account_number(account.getAccount_number())
                .account_number_code(account.getAccount_number_code())
                .account_type(account.getAccount_type())
                .address(account.getAddress())
                .bank_id(account.getBank_id())
                .bank_id_code(account.getBank_id_code())
                .name(account.getName())
                .build();
    }

    private static ChargesInformation toChargesInformation(PaymentDocument paymentDocument) {
        return ChargesInformation.builder()
                .bearer_code(paymentDocument.getBearerCode())
                .sender_charges(toSenderChargesList(paymentDocument.getSenderCharges()))
                .receiver_charges_amount(paymentDocument.getReceiverChargesAmount())
                .receiver_charges_currency(paymentDocument.getReceiverChargesCurrency())
                .build();
    }

    private static List<SenderCharges> toSenderChargesList(List<PaymentDocument.SenderCharges> senderChargesList) {
        return senderChargesList.stream().map(com.example.payments.v0.dto.Payment::toSenderCharges).collect(Collectors.toList());
    }

    private static SenderCharges toSenderCharges(PaymentDocument.SenderCharges senderCharges) {
        return SenderCharges.builder()
                .amount(senderCharges.getAmount())
                .currency(senderCharges.getCurrency())
                .build();
    }
}
