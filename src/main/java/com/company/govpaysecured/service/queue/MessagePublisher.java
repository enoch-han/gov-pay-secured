package com.company.govpaysecured.service.queue;

import com.company.govpaysecured.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessagePublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);

    public boolean publishMessage(String queue, Message message) {
        try {
            jmsTemplate.convertAndSend(queue, message);
            return true;
        } catch (Exception e) {
            LOGGER.error("MessagePublisher_publishMessage_error: {}", e.getMessage());
            return false;
        }
    }
}
