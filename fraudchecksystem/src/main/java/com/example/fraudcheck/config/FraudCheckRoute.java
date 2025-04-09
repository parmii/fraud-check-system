package com.example.fraudcheck.config;

import com.example.fraudcheck.service.FraudCheckService;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.utils.PayloadConverter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FraudCheckRoute extends RouteBuilder {

    private final AuditLogService auditLogService;

    private final FraudCheckService fraudCheckService;

    @Autowired
    public FraudCheckRoute(AuditLogService auditLogService,  FraudCheckService fraudCheckService) {
        this.auditLogService = auditLogService;
        this.fraudCheckService = fraudCheckService;
    }

    @Override
    public void configure() throws Exception {
        from("kafka:fraud-check-request?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest")
            .process(exchange -> {
                String xmlBody = exchange.getIn().getBody(String.class);
                // Convert XML to Object
                FraudCheckRequest payment =PayloadConverter.jsonToObject(PayloadConverter.xmlToJson(xmlBody, FraudCheckRequest.class), FraudCheckRequest.class);

                auditLogService.logEvent(payment.getTransactionId(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_FCS.getEventType(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_FCS.getEventType(), "fraud-check-request");

                FraudCheckResponse response = fraudCheckService.checkFraud(payment);
                exchange.getIn().setBody(PayloadConverter.jsonToXml(PayloadConverter.objectToJson(response), FraudCheckResponse.class));

                auditLogService.logEvent(payment.getTransactionId(), Event.FRAUD_CHECK_REQUEST_SENT_TO_BS.getEventType(), Event.FRAUD_CHECK_REQUEST_SENT_TO_BS.getEventType(), "fraud-check-result");
            })
            .to("kafka:fraud-check-result?brokers=localhost:9092")  // Send result to Kafka result topic
            .onException(Exception.class)
                .log("Exception in route: ${exception.message}")
                .log("Stack Trace: ${exception.stacktrace}") // Prints stack trace
                .process(exchange -> {
                    Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    String xmlBody = exchange.getIn().getBody(String.class);
                    // Convert XML to Object
                    FraudCheckRequest payment =PayloadConverter.jsonToObject(PayloadConverter.xmlToJson(xmlBody, FraudCheckRequest.class), FraudCheckRequest.class);

                    auditLogService.logEvent(payment.getTransactionId(), Event.REQUEST_FAILURE.getEventType(), exception.getMessage(), "");
                })
                .log("Error during fraud check")
                .handled(true);  // Mark exception as handled
    }

}
