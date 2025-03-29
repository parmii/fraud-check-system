package com.example.paymentsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    // Method to log transaction
    public void logEvent(UUID transactionId, String event, String message, String topic) {
        logger.info("Topic: {} - event: {}, transactionId: {}, message: {} received.", topic, event, transactionId, message);
    }

    // Method to log response
    public void logResponse(UUID transactionId, String response) {
        logger.info("TransactionId: {}, message: {} received.", transactionId, response);
    }

    // Method to log general system events, such as application startup
    public void logSystemEvent(String message) {
        logger.info("System Event: {}", message);
    }
}
