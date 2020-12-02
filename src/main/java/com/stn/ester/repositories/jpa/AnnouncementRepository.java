package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.Announcement;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends BaseRepository<Announcement> {
}
