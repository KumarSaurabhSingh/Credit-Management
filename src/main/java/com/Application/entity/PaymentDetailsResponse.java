package com.Application.entity;

public class PaymentDetailsResponse {
    private boolean success;
    private String message;
    private PaymentTransaction paymentTransaction;

    public PaymentDetailsResponse() {
    }

    public PaymentDetailsResponse(boolean success, String message, PaymentTransaction paymentTransaction) {
        this.success = success;
        this.message = message;
        this.paymentTransaction = paymentTransaction;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }
}
