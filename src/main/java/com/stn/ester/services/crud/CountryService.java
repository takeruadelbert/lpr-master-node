package com.stn.ester.services.crud;

import com.stn.ester.entities.Country;
import com.stn.ester.repositories.jpa.CountryRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends CrudService {

    public CountryService(CountryRepository countryRepository) {
        super(countryRepository);
    }
}
