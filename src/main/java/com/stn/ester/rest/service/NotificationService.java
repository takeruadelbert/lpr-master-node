package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.NotificationRepository;
import com.stn.ester.rest.domain.Notification;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService extends AppService {
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        super(Notification.unique_name);
        super.repositories.put(Notification.unique_name, notificationRepository);
        this.notificationRepository = notificationRepository;
    }

    public Object addNotification(Long receiver_id, String message, String url) {
        Notification notification = new Notification(receiver_id, message, url);
        return super.create(notification);
    }

    public Object setToHasSeen(Long notification_id) {
        Notification notification = this.notificationRepository.findById(notification_id).get();
        notification.setToHasSeen();
        return super.update(notification_id, notification);
    }
}
