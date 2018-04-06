package com.example.payments;

import com.example.payments.v0.client.PaymentsClient;
import com.example.payments.v0.dto.Payment;
import com.example.payments.v0.dto.PaymentData;
import com.example.payments.v0.dto.PaymentDataList;
import com.example.payments.v0.dto.PaymentDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import feign.FeignException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest(classes = PaymentsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
@EnableFeignClients(basePackages = "com.example.payments.v0.client")
public class PaymentsSteps {

    @Autowired
    private PaymentsClient paymentsClient;

    @Autowired
    private ObjectMapper mapper;

    private Map<String, ResponseEntity<PaymentData>> payments = new HashMap<>();
    private Map<String, ResponseEntity<PaymentDataList>> paymentsSets = new HashMap<>();

    private static String latestResponse = null;


    @Given("^create new payment with name \"([^\"]*)\" with definition \"([^\"]*)\"$")
    public void create_new_payment_with_name_with_definition(String paymentKey, String paymentDefinitionPath) throws Throwable {
        URL in = this.getClass().getClassLoader().getResource(paymentDefinitionPath);
        val paymentDefinition = mapper.readValue(in, PaymentDefinition.class);
        val responseEntity = paymentsClient.newPayment(paymentDefinition);
        payments.put(paymentKey, responseEntity);
        latestResponse = paymentKey;
    }

    @Then("^find \"([^\"]*)\" as \"([^\"]*)\"$")
    public void find_as(String paymentToFind, String resultPaymentKey) throws Throwable {
        val responseEntity = payments.get(paymentToFind);
        val paymentToFound = responseEntity.getBody();
        val foundResponseEntity = paymentsClient.findPayment(paymentToFound.getData().getId());
        payments.put(resultPaymentKey, foundResponseEntity);
        latestResponse = resultPaymentKey;
    }

    @Then("^last status is (\\d+)$")
    public void last_status_is(int status) throws Throwable {
        val response = payments.get(latestResponse);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.valueOf(status))));
    }

    @Then("^update payment \"([^\"]*)\" with definition \"([^\"]*)\" as \"([^\"]*)\"$")
    public void update_payment_with_definition_as(String paymentKey, String paymentDefinitionPath, String resultPaymentKey) throws Throwable {
        URL in = this.getClass().getClassLoader().getResource(paymentDefinitionPath);
        val updatePaymentDefinition = mapper.readValue(in, PaymentDefinition.class);
        val paymentResonse = payments.get(paymentKey);
        val paymentToUpdate = paymentResonse.getBody();
        val responseEntity = paymentsClient.updatePayment(paymentToUpdate.getData().getId(), updatePaymentDefinition);
        payments.put(resultPaymentKey, responseEntity);
        latestResponse = resultPaymentKey;
    }


    @Given("^a payment with name \"([^\"]*)\" with definition \"([^\"]*)\"$")
    public void a_payment_with_name_with_definition(String paymentKey, String filepath) throws Throwable {
        URL in = this.getClass().getClassLoader().getResource(filepath);
        val payment = mapper.readValue(in, Payment.class);
        payments.put(paymentKey, ResponseEntity.ok(new PaymentData(payment)));

    }

    @Given("^delete \"([^\"]*)\" as \"([^\"]*)\"$")
    public void delete_as(String paymentKey, String deletedKey) throws Throwable {
        val paymentResonse = payments.get(paymentKey);
        val paymentToDelete = paymentResonse.getBody();
        val responseEntity = paymentsClient.deletePayment(paymentToDelete.getData().getId());
        payments.put(deletedKey, responseEntity);
        latestResponse = deletedKey;
    }

    @Then("^\"([^\"]*)\" is equals as \"([^\"]*)\"$")
    public void is_equals_as(String paymentKey1, String paymentKey2) throws Throwable {
        val responseEntity1 = payments.get(paymentKey1);
        val responseEntity2 = payments.get(paymentKey2);
        val payment1 = responseEntity1.getBody();
        val payment2 = responseEntity2.getBody();
        assertThat("payments are equal: ", payment1, equalTo(payment2));
    }


    @Then("^get all payments as \"([^\"]*)\"$")
    public void get_all_payments_as(String paymentSet) throws Throwable {
        try {
            val responseEntity = paymentsClient.findPayments();
            paymentsSets.put(paymentSet, responseEntity);
        } catch (FeignException e) {
            throw e;
        }
    }

    @Then("^payment \"([^\"]*)\" is in \"([^\"]*)\"$")
    public void payment_is_in(String paymentKey, String paymentSetKey) throws Throwable {
        val responseEntity = payments.get(paymentKey);
        val responseEntitySets = paymentsSets.get(paymentSetKey);
        val paymentSet = responseEntitySets.getBody().getData();
        val payment = responseEntity.getBody().getData();
        assertThat(paymentSet.contains(payment), is(true));

    }
}