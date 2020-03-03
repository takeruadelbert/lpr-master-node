package com.stn.ester.core.events.listener;

import com.stn.ester.core.events.LoginEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<LoginEvent> {
    @Override
    public void onApplicationEvent(LoginEvent loginEvent) {
        System.out.printf("User Login : %s from %s %n", loginEvent.getUser().getUsername(), loginEvent.getAddress());
    }
}
