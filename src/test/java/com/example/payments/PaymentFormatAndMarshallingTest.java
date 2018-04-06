package com.example.payments;

import com.example.payments.v0.dto.Payment;
import com.example.payments.v0.dto.PaymentDefinition;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class PaymentFormatAndMarshallingTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setUpClass() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testPaymentDefinitionMarshalling() throws IOException {
        val in = this.getClass().getClassLoader().getResource("newPayment.json");
        val readTree = mapper.readTree(in);
        val fileString = mapper.writeValueAsString(readTree);

        val paymentDefinition = mapper.readValue(in, PaymentDefinition.class);
        val definitionString = mapper.writeValueAsString(paymentDefinition);

        assertThat(definitionString, is(equalTo(fileString)));
    }

    @Test
    public void testPaymentMarshalling() throws IOException {
        val in = this.getClass().getClassLoader().getResource("nonExistingPayment.json");
        val readTree = mapper.readTree(in);
        val fileString = mapper.writeValueAsString(readTree);

        val payment = mapper.readValue(in, Payment.class);
        val paymentString = mapper.writeValueAsString(payment);

        assertThat(paymentString, is(equalTo(fileString)));
    }


}
