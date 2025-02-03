package com.Application.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.Application.Repository.LoanRepository;

@Service
public class CreditLimitScheduler {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    // Schedule the method to run at regular intervals
    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the first day of every month
    public void scheduleCreditLimitUpdate() {
        // Perform credit limit assessment for all borrowers
        List<String> borrowerIds = getAllBorrowerIds(); // Implement a method to retrieve all borrower IDs

        for (String borrowerId : borrowerIds) {
            loanService.assessBorrowerPerformanceAndUpdateCreditLimit(borrowerId);
        }
    }

    private List<String> getAllBorrowerIds() {
        // Implement the logic to retrieve all borrower IDs from your data source
        // This can be done using the appropriate repository or service
        // For example, you can fetch all distinct borrower IDs from the Loan table
        List<String> borrowerIds = loanRepository.findDistinctBorrowerIds();
        return borrowerIds;
    }
}

