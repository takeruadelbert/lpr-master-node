package com.stn.ester.dto.entity;

import com.stn.ester.entities.NotificationRule;
import lombok.Data;

@Data
public class NotificationRuleDTO {

    private String type;
    private String target;
    private String action;

    public NotificationRuleDTO(NotificationRule notificationRule) {
        this.type = notificationRule.getType();
        this.target = notificationRule.getTarget();
        this.action = notificationRule.getAction();
    }
}
