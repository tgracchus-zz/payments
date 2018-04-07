package com.example.payments.service;

import com.example.payments.repository.PaymentDocument;
import com.example.payments.repository.PaymentRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class DefaultPaymentsService implements PaymentsService {

    private final PaymentRepository paymentRepository;

    public DefaultPaymentsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Mono<PaymentDocument> create(PaymentDocument newPaymentDocument) {
        return Mono.just(newPaymentDocument)
                .flatMap(payment ->
                        paymentRepository.insert(payment)
                )
                .onErrorResume(
                        throwable -> {
                            if (throwable instanceof DuplicateKeyException) {
                                return Mono.error(new PaymentAlreadyExist(String.format("PaymentDocument with id %s already exist", newPaymentDocument.getId())));
                            } else {
                                return Mono.error(throwable);
                            }
                        }
                )
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<PaymentDocument> update(PaymentDocument newPaymentDocument) {
        return Mono.just(newPaymentDocument)
                .flatMap(payment ->
                        paymentRepository.save(payment)
                )
                .subscribeOn(Schedulers.elastic());
    }


    @Override
    public Mono<PaymentDocument> find(UUID paymentId) {
        return Mono.just(paymentId)
                .flatMap(paymentId1 ->
                        paymentRepository.findById(paymentId1)
                )
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Flux<PaymentDocument> findAll() {
        return Flux.just(new Object())
                .flatMap(object ->
                        paymentRepository.findAll()
                )
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<PaymentDocument> delete(UUID payment) {
        return find(payment)
                .flatMap(paymentResult ->
                        paymentRepository.deleteById(paymentResult.getId())
                                .map(aVoid -> paymentResult)
                )
                .subscribeOn(Schedulers.elastic());
    }
}
