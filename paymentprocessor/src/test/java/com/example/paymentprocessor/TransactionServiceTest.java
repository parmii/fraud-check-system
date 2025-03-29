package com.example.paymentprocessor;

import com.example.paymentprocessor.repository.Transaction;
import com.example.paymentprocessor.repository.TransactionRepository;
import com.example.paymentprocessor.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private UUID transactionId;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();
        transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAmount(100);
        transaction.setCurrencyId(1);
        transaction.setPayeeId(1);
        transaction.setPayeeId(2);
    }

    @Test
    void testGetTransaction() {
        // Mock repository behavior
        when(transactionRepository.getById(transactionId)).thenReturn(transaction);

        // Call service method
        Transaction result = transactionService.getTransaction(transactionId);

        // Assertions
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        verify(transactionRepository, times(1)).getById(transactionId);
    }

    @Test
    void testSaveTransaction() {
        // Mock repository behavior
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Call service method
        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        // Assertions
        assertNotNull(savedTransaction);
        assertEquals(transactionId, savedTransaction.getId());
        verify(transactionRepository, times(1)).save(transaction);
    }
}
