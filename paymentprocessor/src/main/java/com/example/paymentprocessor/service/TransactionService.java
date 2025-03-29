package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.Transaction;
import com.example.paymentprocessor.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getTransaction(UUID uuid) {
        return transactionRepository.getById(uuid);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
