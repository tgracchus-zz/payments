package com.example.payments.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface PaymentRepository extends ReactiveMongoRepository<PaymentDocument, UUID> {
}
