package com.company.govpaysecured.repository;

import com.company.govpaysecured.domain.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    //List<Payment> findByUserId(Long userId);
}
