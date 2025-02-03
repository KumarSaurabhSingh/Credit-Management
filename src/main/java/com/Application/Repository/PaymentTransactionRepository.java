package com.Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.Application.entity.PaymentTransaction;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

	Optional<PaymentTransaction> findByLoanId(Long loanId);
    // You can add custom query methods if needed
}

