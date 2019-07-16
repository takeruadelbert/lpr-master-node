package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.CityRepository;
import com.stn.ester.rest.dao.jpa.CountryRepository;
import com.stn.ester.rest.dao.jpa.StateRepository;
import com.stn.ester.rest.domain.Biodata;
import com.stn.ester.rest.domain.City;
import com.stn.ester.rest.domain.Country;
import com.stn.ester.rest.domain.State;
import com.stn.ester.rest.domain.enumerate.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiodataService extends AppService {

    @Autowired
    public BiodataService(BiodataRepository biodataRepository, CountryRepository countryRepository, StateRepository stateRepository, CityRepository cityRepository) {
        super(Biodata.unique_name);
        super.repositories.put(Biodata.unique_name, biodataRepository);
        super.repositories.put(Country.unique_name, countryRepository);
        super.repositories.put(State.unique_name, stateRepository);
        super.repositories.put(City.unique_name, cityRepository);
    }

    public Map<Gender, String> getGenderList() {
        Map<Gender, String> result = new HashMap<>();
        List<Gender> genders = Gender.toList();
        for (Gender gender : genders) {
            result.put(gender, gender.getLabel());
        }
        return result;
    }
}
