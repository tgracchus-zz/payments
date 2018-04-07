package com.example.payments.v0.controllers;

import com.example.payments.service.PaymentAlreadyExist;
import com.example.payments.service.PaymentsService;
import com.example.payments.v0.dto.Payment;
import com.example.payments.v0.dto.PaymentDataList;
import com.example.payments.v0.dto.PaymentDefinition;
import com.example.payments.v0.dto.PaymentData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v0/payments")
public class PaymentsController {

    private final PaymentsService paymentsService;

    @Autowired
    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @GetMapping(path = "/{paymentId}", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = PaymentData.class,message = "Find payment")
    })
    public Mono<ResponseEntity<PaymentData>> findPayment(@PathVariable("paymentId") UUID paymentId) {
        return paymentsService.find(paymentId)
                .map(Payment::fromPayment)
                .map(PaymentData::new)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = PaymentDataList.class,message = "Find payments")
    })
    public Mono<ResponseEntity<PaymentDataList>> findPayments() {
        return paymentsService.findAll()
                .map(Payment::fromPayment)
                .collect(Collectors.toList())
                .map(PaymentDataList::new)
                .map(ResponseEntity::ok);
    }

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = PaymentData.class,message = "Created payment")
    })
    public Mono<ResponseEntity<PaymentData>> newPayment(@Valid @RequestBody PaymentDefinition newPayment) throws Exception {
        return paymentsService.create(newPayment.toPaymentDocument())
                .map(Payment::fromPayment)
                .map(PaymentData::new)
                .map(payment ->
                        ResponseEntity.created(URI.create(String.format("/v0/payments/%s", payment.getData().getId()))).body(payment)
                )
                .onErrorResume(throwable -> {
                    if (throwable instanceof PaymentAlreadyExist) {
                        return Mono.error(new ResourceException(HttpStatus.CONFLICT, throwable.getMessage()));
                    } else {
                        return Mono.error(new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage()));
                    }
                });
    }

    @PutMapping(path = "/{paymentId}", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = PaymentData.class,message = "Updated or created payment")
    })
    public Mono<ResponseEntity<PaymentData>> updatePayment(@PathVariable("paymentId") UUID paymentId, @Valid @RequestBody PaymentDefinition updatePayment) {
        return paymentsService.update(updatePayment.toPaymentDocument(paymentId))
                .map(Payment::fromPayment)
                .map(PaymentData::new)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{paymentId}", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = PaymentData.class,message = "Deleted payment")
    })
    public Mono<ResponseEntity<PaymentData>> deletePayment(@PathVariable("paymentId") UUID paymentId) {
        return paymentsService.delete(paymentId)
                .map(Payment::fromPayment)
                .map(PaymentData::new)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
