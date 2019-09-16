package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.entities.AccessLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends BaseRepository<AccessLog> {
}
