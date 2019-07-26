package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.AccessLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends AppRepository<AccessLog, IdList> {
}
