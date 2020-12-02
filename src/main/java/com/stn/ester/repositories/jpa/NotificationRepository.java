package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.Notification;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface NotificationRepository extends BaseRepository<Notification> {

    Collection<Notification> findTop10ByReceiverIdOrderByCreatedDateDesc(Long userId);

    Collection<Notification> findTop10ByReceiverIdAndSeenIsFalseOrderByCreatedDateDesc(Long userId);

    Collection<Notification> findAllByTypeAndReceiverId(String type, Long userId);

    Long countAllByReceiverIdAndSeenIsFalse(Long userId);

    @Query(value = "update notification set publish_dt=created_date where publish_dt is null",
            nativeQuery = true)
    @Transactional
    @Modifying
    void fixPublishDt();
}
