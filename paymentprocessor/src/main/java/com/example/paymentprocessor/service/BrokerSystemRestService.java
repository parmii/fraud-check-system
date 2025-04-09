package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.dto.PaymentResponse;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.utils.RestClientUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Log4j2
@Service("BSREST")
@Qualifier("BSREST")

@RequiredArgsConstructor
public class BrokerSystemRestService extends AbstractBrokerService {

    @Autowired
    private AuditLogService auditLogService;

    @Value("${app.broker.endpoint}")
    private String brokerEndpoint;

    @Override
    public void callFraudCheckForPayment(FraudCheckRequest  fraudCheckRequest) {
        try {
            RestClientUtility restClientUtility = new RestClientUtility();
            restClientUtility.postRequest(brokerEndpoint, fraudCheckRequest, PaymentResponse.class, "BROKER_SERVICE");
            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_BS.getEventType(), Event.FRAUD_CHECK_RESPONSE_SENT_TO_BS.getEventType(), "");
        } catch (Exception e) {
            e.printStackTrace();
            auditLogService.logEvent(fraudCheckRequest.getTransactionId(), Event.REQUEST_FAILURE.getEventType(), e.getMessage(), "");
        }
    }

}
