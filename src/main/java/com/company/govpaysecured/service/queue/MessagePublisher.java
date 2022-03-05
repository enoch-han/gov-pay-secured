package com.company.govpaysecured.service.queue;

import com.company.govpaysecured.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessagePublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public boolean publishMessage(String queue, Message message) {
        try {
            jmsTemplate.convertAndSend(queue, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
