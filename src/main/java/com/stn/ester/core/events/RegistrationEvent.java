package com.stn.ester.core.events;

import com.stn.ester.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private User user;

    public RegistrationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
