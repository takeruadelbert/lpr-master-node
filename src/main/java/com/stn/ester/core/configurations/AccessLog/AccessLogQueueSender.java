package com.stn.ester.core.configurations.AccessLog;

import com.stn.ester.entities.AccessLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AccessLogQueueSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Async
    public void send(AccessLog accessLog) {
        try {
            rabbitTemplate.convertAndSend(queue.getName(), accessLog);
            log.debug("Message has successfully been sent to Queue.");
        } catch (Exception ex) {
            log.trace("-- Fail to send message to Queue --\n" + ex.getMessage());
        }
    }
}
