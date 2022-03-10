package com.company.govpaysecured.service;

import com.company.govpaysecured.domain.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.stereotype.Service;

@Service
public class MessageDroolService {

    private static final String NAME_CHECK_URL = "https://mockbin.org/bin/ecf51063-ee81-4026-a22f-a5bdf4988c65";
    private static final String EXPIRY_DATE_CHECK_URL = "https://mockbin.org/bin/65a728a4-0727-4a9b-b482-01a09eaa9b66";
    private static final String PHONE_CHECK_URL = "https://mockbin.org/bin/bfcb33f3-4dbf-4306-ad37-af05e92f8c53";
    private static final String QUEUE_FOR_25 = "paymentQueue25Done";
    private static final String QUEUE_FOR_50 = "paymentQueue50Done";
    private static final String QUEUE_FOR_75 = "paymentQueue75Done";
    private static final String QUEUE_FOR_100 = "paymentQueue100Done";

    private KieSession session;

    private KieContainer container;

    public MessageDroolService(KieContainer container) {
        this.container = container;
        this.session = createSession();
    }

    public KieSession createSession() {
        var ksession = container.newKieSession();
        ksession.setGlobal("nameCheckURL", NAME_CHECK_URL);
        ksession.setGlobal("expiryDateCheckURL", EXPIRY_DATE_CHECK_URL);
        ksession.setGlobal("phoneCheckURL", PHONE_CHECK_URL);
        ksession.setGlobal("queueFor25", QUEUE_FOR_25);
        ksession.setGlobal("queueFor50", QUEUE_FOR_50);
        ksession.setGlobal("queueFor75", QUEUE_FOR_75);
        ksession.setGlobal("queueFor100", QUEUE_FOR_100);
        return ksession;
    }

    public void insertOrUpdate(Message message) {
        if (message == null) {
            return;
        }
        var factHandle = session.getFactHandle(message);
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
