package com.company.govpaysecured.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "worldline", ignoreUnknownFields = false)
public class Worldline {

    private String apiKeyId = "";
    private String secretApiKey = "";
    private String merchantId = "";
    private String merchantName = "";
    private String returnURL = "";

    public String getApiKeyId() {
        return this.apiKeyId;
    }

    public void setApiKeyId(String apiKeyId) {
        this.apiKeyId = apiKeyId;
    }

    public String getSecretApiKey() {
        return this.secretApiKey;
    }

    public void setSecretApiKey(String secretApiKey) {
        this.secretApiKey = secretApiKey;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getReturnURL() {
        return this.returnURL;
    }

    public void setReturnURL(String returnUrl) {
        this.returnURL = returnUrl;
    }
}
