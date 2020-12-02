package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.entities.Country;
import com.stn.ester.services.crud.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController extends CrudController<CountryService, Country> {
    @Autowired
    public CountryController(CountryService countryService) {
        super(countryService);
    }

    @Override
    @RequireLogin
    public Object list() {
        return super.list();
    }
}
