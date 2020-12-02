package com.stn.ester.services;

import com.stn.ester.entities.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketService {
    public static final String ANNOUNCEMENT_TOPIC = "/topic/announcement";

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendAnnouncement(Announcement announcement) {
        this.simpMessagingTemplate.convertAndSend(ANNOUNCEMENT_TOPIC, announcement);
    }
}
