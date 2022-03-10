package com.company.govpaysecured.config;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfiguration {

    private final KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieContainer getKieContainer() {
        var kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("drools/payment.drl"));
        var kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        var kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
