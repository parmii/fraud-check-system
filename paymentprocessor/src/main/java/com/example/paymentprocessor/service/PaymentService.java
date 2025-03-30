package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentprocessor.repository.PaymentTransactionRepository;
import com.example.paymentprocessor.repository.Transaction;
import com.example.paymentsystem.dto.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentTransactionRepository repository;

    public PaymentTransaction getPaymentTransaction(UUID uuid) {
        Optional<PaymentTransaction> result = repository.findById(uuid);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public PaymentTransaction savePayment(PaymentTransaction transaction) {
        return repository.save(transaction);
    }

    public void updatePaymentStatus(UUID transactionId, String status) {
        repository.findById(transactionId).ifPresent(tx -> {
            tx.setStatus(status);
            repository.save(tx);
        });
    }
}
