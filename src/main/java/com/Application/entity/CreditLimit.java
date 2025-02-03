package com.Application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_limit")
public class CreditLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "borrower_id")
    private String borrowerId;

    @Column(name = "credit_limit")
    private double creditLimit;

    @Column(name = "used_amount")
    private double usedAmount;

    @Column
    private double remainingAmount;

    // Constructors
    public CreditLimit() {
    }

    public CreditLimit(String borrowerId, double creditLimit, double usedAmount) {
        this.borrowerId = borrowerId;
        this.creditLimit = creditLimit;
        this.usedAmount = usedAmount;
        calculateRemainingAmount();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getUsedAmount() {
        return usedAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
        calculateRemainingAmount();
    }

    public void setUsedAmount(double usedAmount) {
        this.usedAmount = usedAmount;
        calculateRemainingAmount();
    }

    // Calculate remaining amount after loading from the database
    @PostLoad
    private void calculateRemainingAmount() {
        remainingAmount = creditLimit - usedAmount;
    }
}
