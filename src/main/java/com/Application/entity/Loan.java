package com.Application.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "borrower_id")
    private String borrowerId;

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "loan_date")
    private LocalDate loanDate;

    @Column(name = "repayment_date")
    private LocalDate repaymentDate;

    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "paid_date")
    private LocalDate paidDate;

    // Constructors
    public Loan() {
    }

    public Loan(String borrowerId, double loanAmount, LocalDate loanDate, LocalDate repaymentDate, String paymentStatus,LocalDate paidDate) {
        this.borrowerId = borrowerId;
        this.loanAmount = loanAmount;
        this.loanDate = loanDate;
        this.repaymentDate = repaymentDate;
        this.paymentStatus = paymentStatus;
        this.paidDate=paidDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getRepaymentDate() {
        return repaymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setRepaymentDate(LocalDate repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public LocalDate getPaidDate() {
        return paidDate;
    }
    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }
}

