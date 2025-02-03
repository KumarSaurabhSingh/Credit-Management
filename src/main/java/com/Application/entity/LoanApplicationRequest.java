package com.Application.entity;

public class LoanApplicationRequest {
    private String borrowerId;
    private double loanAmount;
    
    public LoanApplicationRequest() {
    }
    
    public LoanApplicationRequest(String borrowerId, double loanAmount) {
        this.borrowerId = borrowerId;
        this.loanAmount = loanAmount;
    }
    
    public String getBorrowerId() {
        return borrowerId;
    }
    
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    public double getLoanAmount() {
        return loanAmount;
    }
    
    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }
}


