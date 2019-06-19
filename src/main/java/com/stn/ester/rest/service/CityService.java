package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.CityRepository;
import com.stn.ester.rest.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CityService extends AppService {
    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        super(City.unique_name);
        super.repositories.put(City.unique_name, cityRepository);
        this.cityRepository = cityRepository;
    }

    public Object getCityListByState(long state_id) {
        HashMap<Long,String> result = new HashMap<>();
        List<City> cities = this.cityRepository.findAllByStateId(state_id);
        if(!cities.isEmpty()) {
            for(City city : cities) {
                result.put(city.getId(), city.getName());
            }
        }
        return result;
    }
}
