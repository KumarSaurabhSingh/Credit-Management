package com.Application.entity;

public class RepaymentRequest {
    private double repaymentAmount;

    public RepaymentRequest() {
    }

    public RepaymentRequest(double repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public double getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(double repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }
}

