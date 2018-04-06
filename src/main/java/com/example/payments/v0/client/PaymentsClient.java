package com.example.payments.v0.client;

import com.example.payments.v0.dto.PaymentData;
import com.example.payments.v0.dto.PaymentDataList;
import com.example.payments.v0.dto.PaymentDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(value = "payments", decode404 = true)
public interface PaymentsClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v0/payments/{paymentId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PaymentData> findPayment(@PathVariable("paymentId") UUID paymentId);

    @RequestMapping(method = RequestMethod.GET, value = "/v0/payments", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PaymentDataList> findPayments();

    @RequestMapping(method = RequestMethod.POST, value = "/v0/payments", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PaymentData> newPayment(@RequestBody PaymentDefinition newPayment);

    @RequestMapping(method = RequestMethod.PUT, value = "/v0/payments/{paymentId}", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PaymentData> updatePayment(@PathVariable("paymentId") UUID paymentId, @RequestBody PaymentDefinition newPayment);

    @RequestMapping(method = RequestMethod.DELETE, value = "/v0/payments/{paymentId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PaymentData> deletePayment(@PathVariable("paymentId") UUID paymentId);

}
