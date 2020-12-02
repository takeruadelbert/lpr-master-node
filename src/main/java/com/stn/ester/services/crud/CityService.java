package com.stn.ester.services.crud;

import com.stn.ester.entities.City;
import com.stn.ester.entities.enumerate.CityStatus;
import com.stn.ester.repositories.jpa.CityRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityService extends CrudService {
    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        super(cityRepository);
        this.cityRepository = cityRepository;
    }

    public Object getCityListByState(long state_id) {
        HashMap<Long, String> result = new HashMap<>();
        List<City> cities = this.cityRepository.findAllByStateId(state_id);
        if (!cities.isEmpty()) {
            for (City city : cities) {
                result.put(city.getId(), city.getName());
            }
        }
        return result;
    }

    public Map<CityStatus, String> getCityStatusList() {
        Map<CityStatus, String> result = new HashMap<>();
        List<CityStatus> cityStatuses = CityStatus.toList();
        for (CityStatus cityStatus : cityStatuses) {
            result.put(cityStatus, cityStatus.getLabel());
        }
        return result;
    }
}
