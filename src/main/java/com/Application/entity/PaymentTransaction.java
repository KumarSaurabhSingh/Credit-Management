package com.Application.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "penalty_amount")
    private double penaltyAmount;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "repayment_date")
    private LocalDate repaymentDate;

    @Column(name = "paid_on_date")
    private LocalDate paidOnDate;

    @Column(name = "payment_status")
    private String paymentStatus;

    // Constructors
    public PaymentTransaction() {
    }

    public PaymentTransaction(Long loanId, double loanAmount, double penaltyAmount, double totalAmount, LocalDate repaymentDate, LocalDate paidOnDate, String paymentStatus) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.penaltyAmount = penaltyAmount;
        this.totalAmount = totalAmount;
        this.repaymentDate = repaymentDate;
        this.paidOnDate = paidOnDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getRepaymentDate() {
        return repaymentDate;
    }

    public LocalDate getPaidOnDate() {
        return paidOnDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setRepaymentDate(LocalDate repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public void setPaidOnDate(LocalDate paidOnDate) {
        this.paidOnDate = paidOnDate;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
