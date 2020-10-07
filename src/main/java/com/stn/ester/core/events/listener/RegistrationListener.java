package com.stn.ester.core.events.listener;

import com.google.gson.Gson;
import com.stn.ester.core.events.RegistrationEvent;
import com.stn.ester.entities.User;
import com.stn.ester.services.NotificationService;
import com.stn.ester.services.crud.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class RegistrationListener implements ApplicationListener<RegistrationEvent> {

    private NotificationService notificationService;
    private UserService userService;

    @Autowired
    public RegistrationListener(NotificationService notificationService,
                                UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(RegistrationEvent registrationEvent) {
        User user = registrationEvent.getUser();
        log.info(String.format("New User Register : %s %n", user.getUsername()));
        String message = String.format("%s registered", user.getBiodata().getFullname());
        Map<String, Object> data = new HashMap<>();
        Map<String, String> filter = new HashMap<>();
        filter.put("username", user.getUsername());
        data.put("filter", filter);
        Gson gson = new Gson();
        for (Long superAdminId : userService.getAllSuperAdminId()) {
            this.notificationService.addNotification(superAdminId, message, "#", gson.toJson(data), "user");
        }
    }
}
