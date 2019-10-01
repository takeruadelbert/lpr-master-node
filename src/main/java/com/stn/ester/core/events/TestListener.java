package com.stn.ester.core.events;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TestListener implements ApplicationListener<RegistrationEvent> {
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        System.out.println(event.getUser().getEmail());
    }
}
