package com.stn.ester.dto.entity;

import com.stn.ester.entities.Notification;
import com.stn.ester.helpers.GlobalFunctionHelper;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class NotificationDTO implements Comparable<NotificationDTO> {

    public Long notificationId;
    public Long userId;
    public String message;
    public String url;
    public Boolean seen;
    public LocalDateTime publishTime;
    public Map<String, Object> data;
    public String type;

    public NotificationDTO(Notification notification) {
        this.userId = notification.getReceiverId();
        this.message = notification.getMessage();
        this.url = notification.getUrl();
        this.seen = notification.getSeen();
        this.publishTime = notification.getPublishDt();
        this.type = notification.getType();
        this.notificationId = notification.getId();
        try {
            this.data = GlobalFunctionHelper.jsonStringToMap(notification.getData());
        } catch (IOException e) {
            this.data = new HashMap<>();
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(NotificationDTO o) {
        return o.getPublishTime().compareTo(this.publishTime);
    }
}
