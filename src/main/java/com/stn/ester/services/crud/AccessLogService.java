package com.stn.ester.services.crud;

import com.stn.ester.entities.AccessGroup;
import com.stn.ester.entities.AccessLog;
import com.stn.ester.repositories.jpa.AccessLogRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService extends CrudService {
    public AccessLogService(AccessLogRepository accessLogRepository) {
        super(AccessGroup.class, accessLogRepository);
        super.repositories.put(AccessLog.class.getName(), accessLogRepository);
    }
}
