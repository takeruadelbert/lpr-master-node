package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.CityRepository;
import com.stn.ester.rest.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AppService {
    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        super(City.unique_name);
        super.repositories.put(City.unique_name, cityRepository);
        this.cityRepository = cityRepository;
    }
}
