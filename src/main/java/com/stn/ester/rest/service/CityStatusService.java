package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.CityStatusRepository;
import com.stn.ester.rest.domain.CityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityStatusService extends AppService {
    @Autowired
    public CityStatusService(CityStatusRepository cityStatusRepository) {
        super(CityStatus.unique_name);
        super.repositories.put(CityStatus.unique_name, cityStatusRepository);
    }
}
