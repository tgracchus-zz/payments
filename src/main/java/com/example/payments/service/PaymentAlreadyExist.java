package com.example.payments.service;


public class PaymentAlreadyExist extends RuntimeException {

    public PaymentAlreadyExist(String message) {
        super(message);
    }
}
