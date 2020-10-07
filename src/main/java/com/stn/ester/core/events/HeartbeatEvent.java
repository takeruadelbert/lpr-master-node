package com.stn.ester.core.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class HeartbeatEvent extends ApplicationEvent {

    private Long userId;
    private HttpServletRequest request;
    private String remoteAddress;

    public HeartbeatEvent(Object source, Long userId, HttpServletRequest request) {
        super(source);
        this.userId = userId;
        this.request = request;
    }

    public HeartbeatEvent(Object source, Long userId, String remoteAddress) {
        super(source);
        this.userId = userId;
        this.remoteAddress = remoteAddress;
    }

}
