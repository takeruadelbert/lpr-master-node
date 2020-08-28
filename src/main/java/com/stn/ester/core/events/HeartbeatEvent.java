package com.stn.ester.core.events;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

@Data
public class HeartbeatEvent extends ApplicationEvent {

    private Long userId;
    private HttpServletRequest request;

    public HeartbeatEvent(Object source, Long userId, HttpServletRequest request) {
        super(source);
        this.userId = userId;
        this.request = request;
    }
}
