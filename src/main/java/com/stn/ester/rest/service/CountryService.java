package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.CountryRepository;
import com.stn.ester.rest.domain.Country;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends AppService {

    public CountryService(CountryRepository countryRepository) {
        super(Country.unique_name);
        super.repositories.put(Country.unique_name, countryRepository);
    }
}
