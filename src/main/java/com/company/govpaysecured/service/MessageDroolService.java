package com.company.govpaysecured.service;

import com.company.govpaysecured.domain.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.stereotype.Service;

@Service
public class MessageDroolService {

    private final String nameCheckURL = "https://mockbin.org/bin/ecf51063-ee81-4026-a22f-a5bdf4988c65";
    private final String expiryDateCheckURL = "https://mockbin.org/bin/65a728a4-0727-4a9b-b482-01a09eaa9b66";
    private final String phoneCheckURL = "https://mockbin.org/bin/bfcb33f3-4dbf-4306-ad37-af05e92f8c53";
    private final String queueFor25 = "paymentQueue25Done";
    private final String queueFor50 = "paymentQueue50Done";
    private final String queueFor75 = "paymentQueue75Done";
    private final String queueFor100 = "paymentQueue100Done";

    private KieSession session;

    private KieContainer container;

    public MessageDroolService(KieContainer container) {
        this.container = container;
        this.session = createSession();
    }

    public KieSession createSession() {
        KieSession ksession = container.newKieSession();
        ksession.setGlobal("nameCheckURL", nameCheckURL);
        ksession.setGlobal("expiryDateCheckURL", expiryDateCheckURL);
        ksession.setGlobal("phoneCheckURL", phoneCheckURL);
        ksession.setGlobal("queueFor25", queueFor25);
        ksession.setGlobal("queueFor50", queueFor50);
        ksession.setGlobal("queueFor75", queueFor75);
        ksession.setGlobal("queueFor100", queueFor100);
        return ksession;
    }

    public void insertOrUpdate(Message message) {
        if (message == null) {
            return;
        }
        FactHandle factHandle = session.getFactHandle(message);
        if (factHandle == null) {
            session.insert(message);
            session.fireAllRules();
        } else {
            session.update(factHandle, message);
            session.fireAllRules();
        }
    }

    public void executeRules() {
        session.fireAllRules();
    }

    public void terminate() {
        session.dispose();
    }
}
