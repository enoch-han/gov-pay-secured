package com.company.govpaysecured.service;

import com.company.govpaysecured.domain.Payment;
import com.company.govpaysecured.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment save(Payment payment) {
        log.debug("Payment-PService-SavePayment : {}", payment);
        return paymentRepository.save(payment);
    }
}
