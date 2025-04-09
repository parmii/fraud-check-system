package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.dto.PaymentResponse;
import com.example.paymentsystem.dto.PaymentStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;

public abstract class AbstractBrokerService implements BrokerService {

    public abstract void callFraudCheckForPayment(FraudCheckRequest fraudCheckRequest);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public PaymentResponse sendRequest(Payment payment) {

        PaymentResponse paymentResponse = new PaymentResponse();
        try {
            PaymentTransaction paymentTransaction = paymentService.getPaymentTransaction(payment.getTransactionId());
            String validationMsg = null;


            if (paymentTransaction == null) {
                paymentTransaction = new PaymentTransaction();
                BeanUtils.copyProperties(payment, paymentTransaction);

                paymentService.savePayment(paymentTransaction);
            } else {
                validationMsg = validateTransaction(paymentTransaction);
            }

            if (StringUtils.isNotEmpty(validationMsg)) {
                paymentResponse.setTransactionId(payment.getTransactionId());
                paymentResponse.setMessage(validationMsg);
            } else {
                FraudCheckRequest fraudCheckRequest = new FraudCheckRequest();

                paymentTransaction.setStatus(PaymentStatus.FRAUD_CHECK_INITAITED.getStatus());
                paymentService.savePayment(paymentTransaction);

                BeanUtils.copyProperties(paymentTransaction, fraudCheckRequest);

                Executors.newCachedThreadPool().execute(new BrokerRequestHandler(fraudCheckRequest));

                paymentResponse.setTransactionId(paymentTransaction.getTransactionId());
                paymentResponse.setMessage("Payment sent for Fraud Check.");
          }

        } catch(Exception e) {
            paymentResponse.setTransactionId(payment.getTransactionId());
            paymentResponse.setMessage(e.getMessage());
        }
        return paymentResponse;
    }

    class BrokerRequestHandler implements Runnable {
        private final FraudCheckRequest fraudCheckRequest;

        BrokerRequestHandler(FraudCheckRequest fraudCheckRequest) {
            this.fraudCheckRequest = fraudCheckRequest;
        }

        public void run() {
            callFraudCheckForPayment(fraudCheckRequest);
        }
    }

    private String validateTransaction(PaymentTransaction paymentTransaction) {
        StringBuilder message = new StringBuilder();

        if (paymentTransaction != null && paymentTransaction.getStatus() != null) {
            if (PaymentStatus.FAILED.getStatus().equals(paymentTransaction.getStatus())
                    || PaymentStatus.REJECTED.getStatus().equals(paymentTransaction.getStatus())) {
                message.append("Payment Transaction is failed already.");
            }
            if (PaymentStatus.FRAUD_CHECK_INITAITED.getStatus().equals(paymentTransaction.getStatus())) {
                message.append("Payment Transaction is already in process of FRAUD CHECK.");
            }

            if (PaymentStatus.APPROVED.getStatus().equals(paymentTransaction.getStatus())) {
                message.append("Payment Transaction is already approved.");
            }
        }

        return message.toString();
    }
}
