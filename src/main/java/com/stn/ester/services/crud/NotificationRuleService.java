package com.stn.ester.services.crud;

import com.stn.ester.entities.NotificationRule;
import com.stn.ester.repositories.jpa.NotificationRuleRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationRuleService extends CrudService<NotificationRule, NotificationRuleRepository> {

    @Autowired
    public NotificationRuleService(NotificationRuleRepository notificationRuleRepository) {
        super(notificationRuleRepository);
    }

    public Iterable<NotificationRule> getAll() {
        Iterable<NotificationRule> notificationRules = this.currentEntityRepository.findAll();
        return notificationRules;
    }
}
