package com.company.govpaysecured.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Govpaysecured.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public final Worldline worldLine = new Worldline();

    public Worldline getWorldLine() {
        return this.worldLine;
    }
}
