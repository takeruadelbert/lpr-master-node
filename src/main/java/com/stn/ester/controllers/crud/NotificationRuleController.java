package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.NotificationRule;
import com.stn.ester.services.crud.NotificationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notification_rules")
public class NotificationRuleController extends CrudController<NotificationRuleService, NotificationRule> {

    @Autowired
    public NotificationRuleController(NotificationRuleService notificationRuleService) {
        super(notificationRuleService);
    }
}
