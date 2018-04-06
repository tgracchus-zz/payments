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
public class PaymentDefinition {

    @NotNull
    private String type;

    @NotNull
    @Min(0)
    private int version;

    @NotNull
    private UUID organisation_id;

    @NotNull
    private PaymentAttributes attributes;


    public PaymentDocument toPaymentDocument() {
        return toPaymentDocument(UUID.randomUUID());
    }

    public PaymentDocument toPaymentDocument(UUID paymentId) {
        val paymentBuilder = PaymentDocument.builder();
        val attributes = this.getAttributes();
        val fx = attributes.getFx();
        val beneficiaryParty = attributes.getBeneficiary_party();
        val sponsorParty = attributes.getSponsor_party();
        val debtorParty = attributes.getDebtor_party();
        val chargerInformation = attributes.getCharges_information();
        val senderCharges = chargerInformation.getSender_charges();

        paymentBuilder
                .id(paymentId)
                .type(this.getType())
                .version(this.getVersion())
                .organizationId(this.getOrganisation_id())
                //Attributes
                .amount(attributes.getAmount())
                .currency(attributes.getCurrency())
                .endToEndReference(attributes.getEnd_to_end_reference())
                .numericReference(attributes.getNumeric_reference())
                .paymentId(attributes.getPayment_id())
                .paymentPurpose(attributes.getPayment_purpose())
                .paymentScheme(attributes.getPayment_scheme())
                .paymentType(attributes.getPayment_type())
                .processingDate(attributes.getProcessing_date())
                .reference(attributes.getReference())
                .schemePaymentSubType(attributes.getScheme_payment_sub_type())
                .schemePaymentType(attributes.getScheme_payment_type())
                //Parties
                .beneficiaryParty(toAccount(beneficiaryParty))
                .sponsorParty(toAccount(sponsorParty))
                .debtorParty(toAccount(debtorParty))
                //Charges information
                .senderCharges(toSenderChargesList(senderCharges))
                .bearerCode(chargerInformation.getBearer_code())
                .receiverChargesAmount(chargerInformation.getReceiver_charges_amount())
                .receiverChargesCurrency(chargerInformation.getReceiver_charges_currency())
                //FX
                .contractReference(fx.getContract_reference())
                .exchangeRate(fx.getExchange_rate())
                .originalAmount(fx.getOriginal_amount())
                .originalCurrency(fx.getOriginal_currency());

        return paymentBuilder.build();
    }

    private static PaymentDocument.Account toAccount(Party party) {
        return PaymentDocument.Account.builder()
                .account_name(party.getAccount_name())
                .account_number(party.getAccount_number())
                .account_number_code(party.getAccount_number_code())
                .account_type(party.getAccount_type())
                .address(party.getAddress())
                .bank_id(party.getBank_id())
                .bank_id_code(party.getBank_id_code())
                .name(party.getName())
                .build();
    }

    private static List<PaymentDocument.SenderCharges> toSenderChargesList(List<SenderCharges> senderCharges) {
        return senderCharges.stream().map(PaymentDefinition::toSenderCharges).collect(Collectors.toList());
    }

    private static PaymentDocument.SenderCharges toSenderCharges(SenderCharges senderCharges) {
        return PaymentDocument.SenderCharges.builder()
                .amount(senderCharges.getAmount())
                .currency(senderCharges.getCurrency())
                .build();
    }


}
