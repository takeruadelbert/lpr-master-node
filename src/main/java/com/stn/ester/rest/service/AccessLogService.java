package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessLogRepository;
import com.stn.ester.rest.domain.AccessLog;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService extends AppService {
    public AccessLogService(AccessLogRepository accessLogRepository) {
        super(AccessLog.unique_name);
        super.repositories.put(AccessLog.unique_name, accessLogRepository);
    }
}
