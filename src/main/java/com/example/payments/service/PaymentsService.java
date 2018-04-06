package com.example.payments.service;

import com.example.payments.repository.PaymentDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentsService {
    Mono<PaymentDocument> create(PaymentDocument newPaymentDocument);

    Mono<PaymentDocument> update(PaymentDocument paymentDocument);

    Mono<PaymentDocument> find(UUID paymentId);

    Mono<PaymentDocument> delete(UUID paymentId);

    Flux<PaymentDocument> findAll();
}
