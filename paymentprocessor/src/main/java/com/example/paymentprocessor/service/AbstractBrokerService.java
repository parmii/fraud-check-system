package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.PaymentTransaction;
import com.example.paymentprocessor.repository.Transaction;
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

            String validationMsg = validateTransaction(paymentTransaction);

            if (StringUtils.isNotEmpty(validationMsg)) {
                paymentResponse.setTransactionId(payment.getTransactionId());
                paymentResponse.setMessage(validationMsg);
            } else {
                FraudCheckRequest fraudCheckRequest = new FraudCheckRequest();

                paymentTransaction.setStatus(PaymentStatus.FRAUD_CHECK_INITAITED);
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

        if(paymentTransaction == null) {
            message.append("Payment Transaction doesn't exist");
            return message.toString();
        }
        if (paymentTransaction.getStatus() != null) {
            if (PaymentStatus.FAILED.equals(paymentTransaction.getStatus())
                            || PaymentStatus.FRAUD_CHECK_FAILED.equals(paymentTransaction.getStatus())) {
                message.append("Payment Transaction is failed already.");
            }
            if (PaymentStatus.FRAUD_CHECK_INITAITED.equals(paymentTransaction.getStatus())) {
                message.append("Payment Transaction is already in process of FRAUD CHECK.");
            }// if(transaction.getStatus().name().equalsIgnoreCase(PaymentStatus.FAILED.name()) || )
        }

        return message.toString();
    }
}
