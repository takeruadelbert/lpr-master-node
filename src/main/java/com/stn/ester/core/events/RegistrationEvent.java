package com.stn.ester.core.events;

import com.stn.ester.entities.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class RegistrationEvent extends ApplicationEvent {
    private User user;

    public RegistrationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
