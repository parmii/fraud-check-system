package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentprocessor.repository.PaymentTransactionRepository;
import com.example.paymentprocessor.repository.Transaction;
import com.example.paymentsystem.dto.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentTransactionRepository repository;

    public PaymentTransaction getPaymentTransaction(UUID uuid) {
        return repository.getById(uuid);
    }

    public PaymentTransaction savePayment(PaymentTransaction transaction) {
        transaction.setTransactionId(UUID.randomUUID());
        return repository.save(transaction);
    }

    public void updatePaymentStatus(UUID transactionId, PaymentStatus status) {
        repository.findById(transactionId).ifPresent(tx -> {
            tx.setStatus(status);
            repository.save(tx);
        });
    }
}
