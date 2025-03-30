package com.example.brokersystem.controller;


import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.PaymentResponse;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.utils.PayloadConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/fraud-check")
public class FraudCheckController {

    @Autowired
    private ProducerTemplate producerTemplate;

    private final ObjectMapper objectMapper;


    public FraudCheckController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private AuditLogService auditLogService;

    @Value("${app.fcs.requestroute}")
    private String fcsRequestRoute;

    @PostConstruct
    public void init() {
        try {
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Endpoint to accept fraud check requests
    @PostMapping("/request")
    public ResponseEntity<PaymentResponse> submitFraudCheck(@RequestBody FraudCheckRequest fraudCheckRequest) {
        try {
            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_BS.getEventType(), Event.FRAUD_CHECK_REQUEST_RECEIVED_BY_BS.getEventType(), "");

            producerTemplate.sendBody(fcsRequestRoute, PayloadConverter.toXml(fraudCheckRequest));

            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.FRAUD_CHECK_REQUEST_SENT_TO_FCS.getEventType(), Event.FRAUD_CHECK_REQUEST_SENT_TO_FCS.getEventType(), "fraud-check-request");

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setTransactionId(fraudCheckRequest.getTransactionId());
            paymentResponse.setMessage("FraudCheckRequest fraud check request submitted successfully.");
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setTransactionId(fraudCheckRequest.getTransactionId());
            paymentResponse.setMessage("Error occurred while submitting fraudCheckRequest fraud check request: " + e.getMessage());

            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.REQUEST_FAILURE.getEventType(), Event.REQUEST_FAILURE.getEventType(), "");

            return ResponseEntity.internalServerError().body(paymentResponse);
        }
    }
}
