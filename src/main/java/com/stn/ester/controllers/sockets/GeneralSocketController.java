package com.stn.ester.controllers.sockets;

import com.stn.ester.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GeneralSocketController {

    private SimpMessagingTemplate brokerMessagingTemplate;
    private NotificationService notificationService;

    @Autowired
    public GeneralSocketController(SimpMessagingTemplate brokerMessagingTemplate,
                                   NotificationService notificationService) {
        this.brokerMessagingTemplate = brokerMessagingTemplate;
        this.notificationService = notificationService;
    }

    @SendTo("/topic/announcement")
    public void announcementTopic() {

    }

    @MessageMapping("/notification/feed/me")
    @SendTo("/topic/notification/feed")
    public Map<String, Object> notificationFeedMe() {
        return notificationService.notificationFeedMe();
    }
}
