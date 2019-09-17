package com.stn.ester.core.configurations.AccessLog;

import com.stn.ester.entities.AccessLog;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessLogQueueSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(AccessLog accessLog) {
        rabbitTemplate.convertAndSend(queue.getName(), accessLog);
    }
}
