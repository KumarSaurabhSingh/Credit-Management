package com.Application.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Application.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	List<Loan> findByBorrowerId(String borrowerId);
    // You can add custom query methods if needed
	 List<Loan> findByBorrowerIdAndPaymentStatusAndRepaymentDateBefore(String borrowerId, String paymentStatus, LocalDate date);
	 @Query("SELECT DISTINCT l.borrowerId FROM Loan l")
	    List<String> findDistinctBorrowerIds();
}
