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

import java.util.Collection;

import static com.stn.ester.entities.constant.EntityConstant.FIELD_CREATED_DATE;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Object addNotification(Long receiver_id, String message, String url, String data, String type) {
        Notification notification = new Notification(receiver_id, message, url, data, type);
        return notificationRepository.save(notification);
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
        return notificationRepository.findAll(spec, PageRequest.of(page, size, Sort.by(FIELD_CREATED_DATE).descending()));
    }

    public Collection<Notification> getNotificationFeedByUserId(Long userId) {
        return this.notificationRepository.findTop10ByReceiverIdOrderByCreatedDateDesc(userId);
    }

    public Long countUnseenNotificationByUserId(Long userId) {
        return this.notificationRepository.countAllByReceiverIdAndSeenIsFalse(userId);
    }
}
