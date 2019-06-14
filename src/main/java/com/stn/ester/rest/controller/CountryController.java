package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Country;
import com.stn.ester.rest.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController extends AppController<CountryService, Country> {
    @Autowired
    public CountryController(CountryService countryService) {
        super(countryService);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object getCountryList() {
        return service.getListById();
    }
}
