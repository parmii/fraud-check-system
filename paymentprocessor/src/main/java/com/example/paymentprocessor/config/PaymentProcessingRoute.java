package com.example.paymentprocessor.config;

import com.example.paymentprocessor.service.PaymentService;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.PaymentStatus;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.utils.PayloadConverter;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class PaymentProcessingRoute extends RouteBuilder {
    private final AuditLogService auditLogService;
    private final PaymentService paymentService;

    public PaymentProcessingRoute(AuditLogService auditLogService, PaymentService paymentService) {
        this.auditLogService = auditLogService;
        this.paymentService = paymentService;
    }

    @Override
    public void configure() throws Exception {
        from("kafka:pps-fraud-check-response?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest")
                // .routeId("fraudCheckResultRoute")
                .log("Received Fraud Check Result: ${body}")
                .process(exchange -> {
                    String json = exchange.getIn().getBody(String.class);
                    if (json == null || json.trim().isEmpty()) {
                        throw new IllegalArgumentException("Received empty JSON payload");
                    }
                    FraudCheckResponse response = PayloadConverter.jsonToObject(json, FraudCheckResponse.class);

                    if (response != null) {
                        auditLogService.logEvent(response.getTransactionId(), Event.FRAUD_CHECK_RESPONSE_RECEIVED_BY_PPS.getEventType(), response.getMessage(), "pps-fraud-check-response");

                        UUID transactionId = response.getTransactionId();

                        if (response.getStatus() == 1) {
                            paymentService.updatePaymentStatus(transactionId, PaymentStatus.APPROVED.getStatus());
                        } else {
                            paymentService.updatePaymentStatus(transactionId, PaymentStatus.REJECTED.getStatus());
                        }
                    }
                }).log("Processed Fraud Check Result");
    }
}
