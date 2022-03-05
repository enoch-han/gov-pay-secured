package com.company.govpaysecured.service;

import com.company.govpaysecured.config.ApplicationProperties;
import com.company.govpaysecured.domain.Payment;
import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.CommunicatorConfiguration;
import com.ingenico.connect.gateway.sdk.java.Factory;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.Address;
import com.ingenico.connect.gateway.sdk.java.domain.definitions.AmountOfMoney;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutRequest;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.CreateHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.GetHostedCheckoutResponse;
import com.ingenico.connect.gateway.sdk.java.domain.hostedcheckout.definitions.HostedCheckoutSpecificInput;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Customer;
import com.ingenico.connect.gateway.sdk.java.domain.payment.definitions.Order;
import java.net.URISyntaxException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorldLinePaymentService {

    private final Logger log = LoggerFactory.getLogger(WorldLinePaymentService.class);

    private final ApplicationProperties applicationProperties;

    private final URL worldlineProperties = getClass().getResource("classpath:WorldLinePayment.properties");

    private Client client;

    public WorldLinePaymentService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    private Client getClient() throws URISyntaxException {
        CommunicatorConfiguration configuration = Factory.createConfiguration(
            this.worldlineProperties.toURI(),
            applicationProperties.getWorldLine().getApiKeyId(),
            applicationProperties.getWorldLine().getSecretApiKey()
        );
        client = Factory.createClient(configuration);
        log.debug("Payment-WLPayment-GetClient: {}", client);
        return client;
    }

    public CreateHostedCheckoutResponse initiatePayment(Payment value) {
        CreateHostedCheckoutRequest body = new CreateHostedCheckoutRequest();
        try {
            client = getClient();
            HostedCheckoutSpecificInput hostedCheckoutSpecificInput = new HostedCheckoutSpecificInput();
            hostedCheckoutSpecificInput.setLocale("en_GB");
            hostedCheckoutSpecificInput.setVariant("100");
            hostedCheckoutSpecificInput.setReturnUrl(applicationProperties.getWorldLine().getReturnURL());

            AmountOfMoney amountOfMoney = new AmountOfMoney();
            amountOfMoney.setAmount(value.getPaymentAmount());
            amountOfMoney.setCurrencyCode("USD");

            Address billingAddress = new Address();
            billingAddress.setCountryCode("US");

            Customer customer = new Customer();
            customer.setBillingAddress(billingAddress);
            customer.setMerchantCustomerId(applicationProperties.getWorldLine().getMerchantId());

            Order order = new Order();
            order.setAmountOfMoney(amountOfMoney);
            order.setCustomer(customer);

            body.setHostedCheckoutSpecificInput(hostedCheckoutSpecificInput);
            body.setOrder(order);
        } catch (URISyntaxException e) {
            log.error("Payment-WLPayment-InitiatePaymentError: {}", e.getMessage());
        }

        CreateHostedCheckoutResponse response = client
            .merchant(applicationProperties.getWorldLine().getMerchantId())
            .hostedcheckouts()
            .create(body);

        log.debug("Payment-WLPayment-InitiatePayment: {}", response);

        return response;
    }

    public GetHostedCheckoutResponse getPaymentResponse(String hostedCheckoutId) {
        try {
            client = getClient();
        } catch (Exception e) {
            log.error("Payment-WLPayment-GetClientError: {}", e.getMessage());
        }

        GetHostedCheckoutResponse response = client
            .merchant(applicationProperties.getWorldLine().getMerchantId())
            .hostedcheckouts()
            .get(hostedCheckoutId);

        log.debug("Payment-WLPayment-GetPaymentResponse: {}", response);

        return response;
    }
}
