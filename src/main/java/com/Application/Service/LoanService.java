package com.Application.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Application.Repository.CreditLimitRepository;
import com.Application.Repository.LoanRepository;
import com.Application.entity.CreditLimit;
import com.Application.entity.Loan;
import com.Application.entity.LoanApplicationRequest;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CreditLimitRepository creditLimitRepository;

    public Loan processLoanApplication(LoanApplicationRequest request) {
        String borrowerId = request.getBorrowerId();
        double loanAmount = request.getLoanAmount();
        
        if (borrowerId == null || borrowerId.isEmpty() || loanAmount <= 0) {
            throw new IllegalArgumentException("Invalid or missing input.");
        }

        // Check if borrower exists in the credit limit table
        Optional<CreditLimit> optionalCreditLimit = creditLimitRepository.findByBorrowerId(borrowerId);
        CreditLimit creditLimit;

        if (optionalCreditLimit.isPresent()) {
            // Borrower exists, retrieve the credit limit
            creditLimit = optionalCreditLimit.get();
        } else {
            // New borrower, create a new entry with base credit limit
            creditLimit = new CreditLimit();
            creditLimit.setBorrowerId(borrowerId);
            creditLimit.setCreditLimit(5000.0);
            creditLimit.setUsedAmount(0.0);
        }

        double currentCreditLimit = creditLimit.getCreditLimit();
        double currentUsedAmount = creditLimit.getUsedAmount();
        double remainingAmount = currentCreditLimit - currentUsedAmount;
        
        // Check if there are any existing unpaid loans with crossed repayment dates
        List<Loan> unpaidLoans = loanRepository.findByBorrowerIdAndPaymentStatusAndRepaymentDateBefore(borrowerId, "Not Paid", LocalDate.now());

        if (!unpaidLoans.isEmpty()) {
            // Reject the loan application due to existing unpaid loans
            throw new IllegalArgumentException("There are existing unpaid loans with crossed repayment dates.");
        }

        // Check if loan amount is within the remaining credit limit
        if (loanAmount <= remainingAmount) {
            // Approve the loan application
            Loan loan = new Loan();
            loan.setBorrowerId(borrowerId);
            loan.setLoanAmount(loanAmount);
            loan.setLoanDate(LocalDate.now());
            loan.setRepaymentDate(loan.getLoanDate().plusMonths(1)); // Set the repayment date to next month
            loan.setPaymentStatus("Not Paid"); // Set the initial payment status
            loanRepository.save(loan);

            // Update the used amount in the credit limit
            double updatedUsedAmount = currentUsedAmount + loanAmount;
            creditLimit.setUsedAmount(updatedUsedAmount);
            creditLimitRepository.save(creditLimit);
            
            return loan;
            
        } else {
            // Reject the loan application
            throw new IllegalArgumentException("Loan amount exceeds the remaining credit limit.");
        }
       
    }

    // Helper method to calculate the penalty amount based on loan amount, repayment date, and current date
    public double calculatePenaltyAmount(double loanAmount, LocalDate repaymentDate, LocalDate currentDate) {
        long daysOverdue = ChronoUnit.DAYS.between(repaymentDate, currentDate);
        double penaltyRate = 0.002; // 0.2% = 0.002
        return loanAmount * penaltyRate * daysOverdue;
    }
    
    public void assessBorrowerPerformanceAndUpdateCreditLimit(String borrowerId) {
        // Retrieve the borrower's credit limit
        Optional<CreditLimit> optionalCreditLimit = creditLimitRepository.findByBorrowerId(borrowerId);
        if (optionalCreditLimit.isPresent()) {
            CreditLimit creditLimit = optionalCreditLimit.get();
            
            // Retrieve the borrower's loan repayment history
            List<Loan> loanHistory = loanRepository.findByBorrowerId(borrowerId);
            
            // Evaluate borrower performance based on the repayment history and defined metric
            boolean meetsPerformanceCriteria = evaluateBorrowerPerformance(loanHistory);
            
            // Update credit limit if the borrower meets the performance criteria
            if (meetsPerformanceCriteria) {
                double currentCreditLimit = creditLimit.getCreditLimit();
                double increasedCreditLimit = calculateIncreasedCreditLimit(currentCreditLimit);
                creditLimit.setCreditLimit(increasedCreditLimit);
                creditLimitRepository.save(creditLimit);
            }
        }
    }
    
    private boolean evaluateBorrowerPerformance(List<Loan> loanHistory) {
        // Define your metric and business rules for evaluating borrower performance
        
        // Example: Evaluate based on on-time repayment
        boolean meetsPerformanceCriteria = true;
        
        for (Loan loan : loanHistory) {
            if (!isLoanRepaidOnTime(loan)) {
                meetsPerformanceCriteria = false;
                break;
            }
        }
        
        return meetsPerformanceCriteria;
    }

    private boolean isLoanRepaidOnTime(Loan loan) {
        LocalDate repaymentDate = loan.getRepaymentDate();
        LocalDate paidDate = loan.getPaidDate();
        
        if (paidDate != null && paidDate.isAfter(repaymentDate)) {
            return false; // Loan was not repaid on time
        }
        
        return true; // Loan was repaid on time
    }
    private double calculateIncreasedCreditLimit(double currentCreditLimit) {
        // Define your logic for calculating the increased credit limit
        
        // Example: Increase by 10% of the current credit limit
        double percentageIncrease = 0.10; // 10%
        double increasedCreditLimit = currentCreditLimit + (currentCreditLimit * percentageIncrease);
        
        return increasedCreditLimit;
    }


}

