package com.stn.ester.core.events;

import com.stn.ester.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginEvent extends ApplicationEvent {
    private User user;
    private String address;
    private LocalDateTime loginTime;

    public LoginEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
