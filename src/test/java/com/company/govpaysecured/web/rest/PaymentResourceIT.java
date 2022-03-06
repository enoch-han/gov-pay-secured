package com.company.govpaysecured.web.rest;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.company.govpaysecured.IntegrationTest;
import com.company.govpaysecured.domain.Payment;
import com.company.govpaysecured.repository.PaymentRepository;
import com.company.govpaysecured.security.AuthoritiesConstants;
import com.company.govpaysecured.service.PaymentService;
import com.company.govpaysecured.service.WorldLinePaymentService;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
public class PaymentResourceIT {

    private Payment testPayment;
    private String testHostedCheckoutId = "061fbdeb-ce14-71ff-901d-5540f82e3ad6";

    @Autowired
    private MockMvc restPaymentMock;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setName("natan");
        testPayment.setCcc("123456789");
        testPayment.setCik("12345678");
        testPayment.setCompanyName("Abc company");
        testPayment.setEmail("natan@gmail.com");
        testPayment.setPaymentAmount(Long.valueOf(12));
        testPayment.setLastPayment(Instant.EPOCH);
        testPayment.setPhoneNumber("1233456789");
        testPayment.setPaymentId("1234567898912315");
    }

    @Test
    @Transactional
    void testIfItCreatesPaymentWithValidPayment() throws Exception {
        System.out.println("hello i am in payment resource test");
        restPaymentMock
            .perform(
                post("/api/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPayment))
                    .with(csrf())
            )
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void testIfItCreatesPaymentWithInvalidPayment() throws Exception {
        testPayment.setId(Long.valueOf(1));
        restPaymentMock
            .perform(
                post("/api/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPayment))
                    .with(csrf())
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testGetCompanyName() throws Exception {
        restPaymentMock.perform(get("/api/payments/companyName").with(csrf())).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testGetLastPayment() throws Exception {
        restPaymentMock.perform(get("/api/payments/lastPayment").with(csrf())).andExpect(status().isOk());
    }
}
