package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends AppRepository<Notification> {
}
