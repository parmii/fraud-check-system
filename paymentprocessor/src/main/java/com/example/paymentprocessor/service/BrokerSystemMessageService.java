package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentsystem.constant.ApplicationConstant;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.utils.JSONMapper;
import com.example.paymentsystem.utils.PayloadConverter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Service("BSMESSENGER")
@Qualifier("BSMESSENGER")
@RequiredArgsConstructor
public class BrokerSystemMessageService extends AbstractBrokerService{
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateS;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private AuditLogService auditLogService;

    @Value("${app.broker.camelRoute}")
    private String brokerEndpoint;

    @Override
    public void callFraudCheckForPayment(FraudCheckRequest fraudCheckRequest) {
        try {
            producerTemplate.sendBody(brokerEndpoint, PayloadConverter.objectToJson(fraudCheckRequest));
            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_BS.getEventType(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_BS.getEventType(), "pps-fraud-check-request");
        } catch (Exception e) {
            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.REQUEST_FAILURE.getEventType(), e.getMessage(), "");
            e.printStackTrace();
        }
    }
}
