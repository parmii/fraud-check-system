package com.example.brokersystem.config;

import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.service.AuditLogService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrokerSystemRoute extends RouteBuilder {

    private final AuditLogService auditLogService;

    public BrokerSystemRoute(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void configure() throws Exception {
        // Convert User Fraud Check JSON Request to XML and Send to FCS
        from("kafka:pps-fraud-check-request?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest")
                .log("Received JSON Fraud Check Request: ${body}")
                .process(exchange -> {
                    FraudCheckRequest request = exchange.getIn().getBody(FraudCheckRequest.class);
                    auditLogService.logEvent(request.getTransactionId(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_BS.getEventType(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_BS.getEventType(), "pps-fraud-check-request");

                    XmlMapper xmlMapper = new XmlMapper();
                    String xmlRequest = xmlMapper.writeValueAsString(request);
                    exchange.getIn().setBody(xmlRequest);

                    auditLogService.logEvent(request.getTransactionId(), Event.FRAUD_CHECK_REQUEST_SENT_TO_FCS.getEventType(), Event.FRAUD_CHECK_REQUEST_SENT_TO_FCS.getEventType(), "fraud-check-request");

                })
                .log("Sending XML Fraud Check Request to FCS: ${body}")
                .to("kafka:fraud-check-request?brokers=localhost:9092");

        from("kafka:fraud-check-result?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest")
            .log("Received Fraud Check Result from FCS: ${body}")
                .process(exchange -> {
                    String xmlBody = exchange.getIn().getBody(String.class);
                    // Convert XML to Object
                    XmlMapper xmlMapper = new XmlMapper();
                    FraudCheckResponse response = xmlMapper.readValue(xmlBody, FraudCheckResponse.class);
                    auditLogService.logEvent(response.getTransactionId(), Event.FRAUD_CHECK_RESPONSE_RECEIVED_BY_BS.getEventType(), Event.FRAUD_CHECK_RESPONSE_RECEIVED_BY_BS.getEventType(), "fraud-check-result");
                    exchange.getIn().setBody(response);
                    auditLogService.logEvent(response.getTransactionId(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_PPS.getEventType(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_PPS.getEventType(), "pps-fraud-check-response");
                })
            .log("Converted XML to JSON: ${body}")
            .to("kafka:pps-fraud-check-response?brokers=localhost:9092") // Publish to Kafka
            .log("Published Fraud Check Result to Kafka for User");
    }
}
