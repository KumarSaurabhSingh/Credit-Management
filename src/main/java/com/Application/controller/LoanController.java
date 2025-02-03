package com.Application.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.Repository.CreditLimitRepository;
import com.Application.Repository.LoanRepository;
import com.Application.Repository.PaymentTransactionRepository;
import com.Application.Service.LoanService;
import com.Application.entity.CreditLimit;
import com.Application.entity.Loan;
import com.Application.entity.LoanApplicationRequest;
import com.Application.entity.PaymentDetailsResponse;
import com.Application.entity.PaymentTransaction;
import com.Application.entity.RepaymentRequest;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    
    @Autowired
    private CreditLimitRepository creditLimitRepository;
    
    @PostMapping("/process")
    public ResponseEntity<Object> applyLoan(@RequestBody LoanApplicationRequest request) {
        try {
        	 Loan loan = loanService.processLoanApplication(request);
        	 
        	 double loanAmount = loan.getLoanAmount();
             LocalDate repaymentDate = loan.getRepaymentDate();
             String paymentStatus = loan.getPaymentStatus();

             // Check if the payment transaction already exists for the given loan ID
             Optional<PaymentTransaction> optionalPaymentTransaction = paymentTransactionRepository.findByLoanId(loan.getId());
             PaymentTransaction paymentTransaction;

             if (optionalPaymentTransaction.isPresent()) {
                 paymentTransaction = optionalPaymentTransaction.get();
                 // Check if the repayment date has passed to calculate the penalty amount
                 LocalDate currentDate = LocalDate.now();
                 if (currentDate.isAfter(repaymentDate)) {
                     double penaltyAmount = loanService.calculatePenaltyAmount(loanAmount, repaymentDate, currentDate);
                     double totalAmount = loanAmount + penaltyAmount;
                     paymentTransaction.setPenaltyAmount(penaltyAmount);
                     paymentTransaction.setTotalAmount(totalAmount);
                     paymentTransaction.setLoanAmount(loanAmount);
                     paymentTransactionRepository.save(paymentTransaction);
                 }
             } else {
                 paymentTransaction = new PaymentTransaction();
                 paymentTransaction.setLoanId(loan.getId());
                 LocalDate currentDate = LocalDate.now();
                 if (currentDate.isAfter(repaymentDate)) {
                     double penaltyAmount =loanService. calculatePenaltyAmount(loanAmount, repaymentDate, currentDate);
                     double totalAmount = loanAmount + penaltyAmount;
                     paymentTransaction.setPenaltyAmount(penaltyAmount);
                     paymentTransaction.setTotalAmount(totalAmount);
                     paymentTransaction.setLoanAmount(loanAmount);
                 }else {
                 paymentTransaction.setLoanAmount(loanAmount);
                 paymentTransaction.setPenaltyAmount(0);
                 paymentTransaction.setTotalAmount(loanAmount);
                 }
                 paymentTransaction.setRepaymentDate(repaymentDate);
                 paymentTransaction.setPaymentStatus(paymentStatus);
                 paymentTransactionRepository.save(paymentTransaction);
             }

//            Map<String, String> response = new HashMap<>();
//            response.put("status", "success");
//            response.put("message", "Loan application approved and transferred");
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<Object> viewAllLoans() {
        List<Loan> loans = loanRepository.findAll();

        if (loans.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "No loan data found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Object> viewLoanById(@PathVariable("loanId") Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);

        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            return ResponseEntity.ok(loan);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Invalid loan ID.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/borrowers/{borrowerId}")
    public ResponseEntity<Object> viewLoansByBorrowerId(@PathVariable("borrowerId") String borrowerId) {
        List<Loan> loans = loanRepository.findByBorrowerId(borrowerId);

        if (loans.isEmpty()) {
            Map<String,Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "No loan data found for borrower ID: " + borrowerId);
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(loans);
    }

    @GetMapping("/creditlimit/{borrowerId}")
    public ResponseEntity<Object> viewCreditLimitByBorrowerId(@PathVariable("borrowerId") String borrowerId) {
        Optional<CreditLimit> optionalCreditLimit = creditLimitRepository.findByBorrowerId(borrowerId);

        if (optionalCreditLimit.isPresent()) {
            CreditLimit creditLimit = optionalCreditLimit.get();
            return ResponseEntity.ok(creditLimit);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "No credit limit data found for borrower ID: " + borrowerId);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/paymentdetails/{loanId}")
    public ResponseEntity<Object> getPaymentDetails(@PathVariable("loanId") Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);

        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();

            // Check if the payment transaction exists for the given loan ID
            Optional<PaymentTransaction> optionalPaymentTransaction = paymentTransactionRepository.findByLoanId(loanId);

            PaymentTransaction paymentTransaction;

            if (optionalPaymentTransaction.isPresent()) {
                paymentTransaction = optionalPaymentTransaction.get();

                // Check if the repayment date has passed to calculate the penalty amount
                LocalDate currentDate = LocalDate.now();
                LocalDate repaymentDate = loan.getRepaymentDate();

                if (currentDate.isAfter(repaymentDate)) {
                    double penaltyAmount = loanService.calculatePenaltyAmount(loan.getLoanAmount(), repaymentDate, currentDate);
                    double totalAmount = loan.getLoanAmount() + penaltyAmount;

                    // Update the payment transaction with the new penalty and total amounts
                    paymentTransaction.setPenaltyAmount(penaltyAmount);
                    paymentTransaction.setTotalAmount(totalAmount);
                }
            } else {
                paymentTransaction = new PaymentTransaction();
                paymentTransaction.setLoanId(loanId);
                paymentTransaction.setLoanAmount(loan.getLoanAmount());
                paymentTransaction.setRepaymentDate(loan.getRepaymentDate());
                paymentTransaction.setPaymentStatus(loan.getPaymentStatus());
            }

            // Save or update the payment transaction
            paymentTransactionRepository.save(paymentTransaction);

            
            return ResponseEntity.ok(paymentTransaction);
        } else {
            PaymentDetailsResponse response = new PaymentDetailsResponse();
            response.setSuccess(false);
            response.setMessage("Invalid loan ID.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/paymentdetails/{loanId}/repayment")
    public ResponseEntity<Object> repayLoan(@PathVariable("loanId") Long loanId, @RequestBody RepaymentRequest request) {
        Optional<PaymentTransaction> optionalPaymentTransaction = paymentTransactionRepository.findByLoanId(loanId);

        if (optionalPaymentTransaction.isPresent()) {
            PaymentTransaction paymentTransaction = optionalPaymentTransaction.get();
            
         // Check if the payment status is already marked as "Paid"
            if ("Paid".equals(paymentTransaction.getPaymentStatus())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success",false);
                response.put("message", "Payment has already been done for this loan.");
                return ResponseEntity.badRequest().body(response);
            }
            double totalAmount = paymentTransaction.getTotalAmount();
            double repaymentAmount = request.getRepaymentAmount();

            if (totalAmount != repaymentAmount) {
            	Map<String, Object> response = new HashMap<>();
                response.put("success",false);
                response.put("message", "Payment cannot be done as you need to pay the full loan amount.");
                return ResponseEntity.badRequest().body(response);
            }

            // Update the payment transaction and loan table
            paymentTransaction.setPaidOnDate(LocalDate.now());
            paymentTransaction.setPaymentStatus("Paid");
            paymentTransactionRepository.save(paymentTransaction);

            Optional<Loan> optionalLoan = loanRepository.findById(loanId);
            if (optionalLoan.isPresent()) {
                Loan loan = optionalLoan.get();
                loan.setPaymentStatus("Paid");
                loan.setPaidDate(LocalDate.now());
                loanRepository.save(loan);
            }
            if (optionalLoan.isPresent()) {

            Loan loan = optionalLoan.get();
            String borrowerId = loan.getBorrowerId();
            Optional<CreditLimit> optionalCreditLimit = creditLimitRepository.findByBorrowerId(borrowerId);

            if (optionalCreditLimit.isPresent()) {
                CreditLimit creditLimit = optionalCreditLimit.get();
                double currentUsedAmount = creditLimit.getUsedAmount();
                double loanAmount = paymentTransaction.getLoanAmount();
                double updatedUsedAmount = currentUsedAmount - loanAmount;
                creditLimit.setUsedAmount(updatedUsedAmount);
                creditLimitRepository.save(creditLimit);
            }
            }
            return ResponseEntity.ok(paymentTransaction);

            
        } else {
        	Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Invalid loan ID.");
            return ResponseEntity.badRequest().body(response);
            
        }
    }
    
    

}
