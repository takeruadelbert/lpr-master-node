package com.stn.ester.services;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.entities.Notification;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.repositories.jpa.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Object addNotification(Long receiver_id, String message, String url, String data, String type) {
        return addNotification(receiver_id, message, url, data, type, LocalDateTime.now());
    }

    public Object addNotification(Long receiver_id, String message, String url, String data, String type, LocalDateTime publishDt) {
        Notification notification = new Notification(receiver_id, message, url, data, type, publishDt);
        return notificationRepository.save(notification);
    }

    public void updateNotification(Long receiver_id, String message, String url, String data, String type) {
        updateNotification(receiver_id, message, url, data, type, LocalDateTime.now());
    }

    @Transactional
    public void updateNotification(Long receiver_id, String message, String url, String data, String type, LocalDateTime publishDt) {
        Collection<Notification> notifications = this.notificationRepository.findAllByTypeAndReceiverId(type, receiver_id);
        if (!notifications.isEmpty()) {
            for (Notification notification : notifications) {
                notification.setSeen(false);
                notification.setUrl(url);
                notification.setData(data);
                notification.setMessage(message);
                notification.setPublishDt(publishDt);
                notificationRepository.save(notification);
            }
        } else {
            addNotification(receiver_id, message, url, data, type, publishDt);
        }
    }

    public Object setToHasSeen(Long notification_id) {
        Notification notification = this.notificationRepository.findById(notification_id).get();
        if (notification.getReceiverId() != SessionHelper.getUserID()) {
            throw new BadRequestException("Wrong user!");
        }
        notification.setSeen(true);
        return notificationRepository.save(notification);
    }

    public Page<Notification> indexByUserId(Long userId, Integer page, Integer size, Specification specification) {
        AppSpecification<Notification> spec1 = new AppSpecification<>(new SpecSearchCriteria("receiverId", SearchOperation.EQUALITY, userId));
        Specification spec = Specification.where(spec1).and(specification);
        return notificationRepository.findAll(spec, PageRequest.of(page, size, Sort.by("publishDt").descending()));
    }

    public Collection<Notification> getNotificationFeedByUserId(Long userId) {
        return this.notificationRepository.findTop10ByReceiverIdAndSeenIsFalseOrderByCreatedDateDesc(userId);
    }

    public Long countUnseenNotificationByUserId(Long userId) {
        return this.notificationRepository.countAllByReceiverIdAndSeenIsFalse(userId);
    }

    @PostConstruct
    public void populatePublishDt() {
        this.notificationRepository.fixPublishDt();
        updateNotification(1L, "ok", "#", "{}", "test");
    }
}
