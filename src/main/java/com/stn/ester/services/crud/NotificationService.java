package com.stn.ester.services.crud;

import com.stn.ester.entities.Notification;
import com.stn.ester.repositories.jpa.NotificationRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService extends CrudService {
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        super(Notification.class, notificationRepository);
        super.repositories.put(Notification.class.getName(), notificationRepository);
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
