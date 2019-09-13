package com.stn.ester.services.crud;

import com.stn.ester.entities.Biodata;
import com.stn.ester.entities.City;
import com.stn.ester.entities.Country;
import com.stn.ester.entities.State;
import com.stn.ester.entities.enumerate.Gender;
import com.stn.ester.repositories.jpa.BiodataRepository;
import com.stn.ester.repositories.jpa.CityRepository;
import com.stn.ester.repositories.jpa.CountryRepository;
import com.stn.ester.repositories.jpa.StateRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiodataService extends CrudService {

    @Autowired
    public BiodataService(BiodataRepository biodataRepository, CountryRepository countryRepository, StateRepository stateRepository, CityRepository cityRepository) {
        super(Biodata.class, biodataRepository);
        super.repositories.put(Biodata.class.getName(), biodataRepository);
        super.repositories.put(Country.class.getName(), countryRepository);
        super.repositories.put(State.class.getName(), stateRepository);
        super.repositories.put(City.class.getName(), cityRepository);
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
