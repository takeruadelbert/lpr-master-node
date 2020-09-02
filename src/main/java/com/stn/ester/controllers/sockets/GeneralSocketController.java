package com.stn.ester.controllers.sockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralSocketController {

    private SimpMessagingTemplate brokerMessagingTemplate;

    @Autowired
    public GeneralSocketController(SimpMessagingTemplate brokerMessagingTemplate) {
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

}
