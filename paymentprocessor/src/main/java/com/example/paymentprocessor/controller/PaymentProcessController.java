package com.example.paymentprocessor.controller;

import com.example.paymentprocessor.service.BrokerService;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@OpenAPI31
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentProcessController {

    @Autowired
    @Qualifier("BSREST")
    private BrokerService brokerServiceRest;

    @Autowired
    @Qualifier("BSMESSENGER")
    private BrokerService brokerServiceMessanger;

    @PostMapping("/processpayment")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody @Schema @Valid Payment payment) {
        PaymentResponse response =  brokerServiceRest.sendRequest(payment);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/processpayment/message")
    public ResponseEntity<PaymentResponse> sendMessageToBroker(@RequestBody @Schema @Valid Payment payment) {
        PaymentResponse response =  brokerServiceMessanger.sendRequest(payment);
        return ResponseEntity.ok().body(response);
    }
}
