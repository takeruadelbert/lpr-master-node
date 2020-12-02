package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.dto.entity.NotificationRuleDTO;
import com.stn.ester.entities.NotificationRule;
import com.stn.ester.services.crud.NotificationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/notification_rules")
public class NotificationRuleController extends CrudController<NotificationRuleService, NotificationRule> {

    @Autowired
    public NotificationRuleController(NotificationRuleService notificationRuleService) {
        super(notificationRuleService);
    }

    @RequireLogin
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public List<NotificationRuleDTO> all() {
        List<NotificationRuleDTO> notificationRuleDTOList = StreamSupport.stream(this.service.getAll().spliterator(), false).map(NotificationRuleDTO::new).collect(Collectors.toList());
        return notificationRuleDTOList;
    }
}
