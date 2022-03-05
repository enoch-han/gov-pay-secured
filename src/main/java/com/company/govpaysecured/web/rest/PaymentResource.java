package com.company.govpaysecured.web.rest;

import com.company.govpaysecured.domain.Message;
import com.company.govpaysecured.domain.Mock;
import com.company.govpaysecured.domain.Payment;
import com.company.govpaysecured.service.WorldLinePaymentService;
import com.company.govpaysecured.service.queue.MessagePublisher;
import com.company.govpaysecured.web.rest.errors.BadRequestAlertException;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.GetHostedCheckoutResponse;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";

    private final MessagePublisher messagePublisher;

    private final WorldLinePaymentService worldLinePaymentService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PaymentResource(WorldLinePaymentService worldLinePaymentService, MessagePublisher messagePublisher) {
        this.worldLinePaymentService = worldLinePaymentService;
        this.messagePublisher = messagePublisher;
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        log.debug("Payment-PResource-CreatePayment-PaymentReceived : {}", payment);
        if (payment.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", "paymentManagement", "idexists");
        }

        Message message = new Message();
        message.setSource("rest resource");
        message.setMessage("payment needs to validated");
        message.setPayload(payment);
        if (messagePublisher.publishMessage("paymentQueue", message)) {
            return ResponseEntity
                .created(new URI("/api/payments/" + payment.getPhoneNumber()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, payment.getPhoneNumber()))
                .body(payment);
        } else {
            return (ResponseEntity) ResponseEntity.badRequest();
        }
    }

    @GetMapping("/payments/companyName")
    public Mock getCompanyName() {
        // a mock that aquires company name
        String uri = "https://mockbin.org/bin/c725e6ca-adbe-4a1a-900a-63fea1c4b760";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    @GetMapping("/payments/lastPayment")
    public Mock getLastPayment() {
        // a mock that aquires last Payment date
        String uri = "https://mockbin.org/bin/5c7b0680-ce8a-41b2-b303-2f9bd9172196";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Mock.class);
    }

    @PostMapping("/payments/initiate")
    public CreateHostedCheckoutResponse getInititatePayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
        CreateHostedCheckoutResponse response = worldLinePaymentService.initiatePayment(payment);
        log.debug(" world line initiation response : {}", response);

        return response;
    }

    @PostMapping("/payments/getPaymentResponse")
    public GetHostedCheckoutResponse getPaymentResponse(@Valid @RequestBody String hostedCheckoutId) throws URISyntaxException {
        return worldLinePaymentService.getPaymentResponse(hostedCheckoutId);
    }
}
