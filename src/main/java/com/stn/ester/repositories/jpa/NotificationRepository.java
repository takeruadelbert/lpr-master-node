package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.Notification;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends BaseRepository<Notification> {

    Collection<Notification> findTop10ByReceiverIdOrderByCreatedDateDesc(Long userId);

    Long countAllByReceiverIdAndSeenIsFalse(Long userId);
}
