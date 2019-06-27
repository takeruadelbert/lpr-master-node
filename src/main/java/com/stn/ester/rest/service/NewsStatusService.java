package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.NewsStatusRepository;
import com.stn.ester.rest.domain.NewsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsStatusService extends AppService {
    @Autowired
    public NewsStatusService(NewsStatusRepository newsStatusRepository) {
        super(NewsStatus.unique_name);
        super.repositories.put(NewsStatus.unique_name, newsStatusRepository);
    }
}
