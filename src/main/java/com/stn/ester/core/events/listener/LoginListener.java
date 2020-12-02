package com.stn.ester.core.events.listener;

import com.stn.ester.core.events.LoginEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class LoginListener implements ApplicationListener<LoginEvent> {
    @Override
    public void onApplicationEvent(LoginEvent loginEvent) {
        log.info(String.format("User Login : %s from %s %n", loginEvent.getUser().getUsername(), loginEvent.getAddress()));
    }
}
