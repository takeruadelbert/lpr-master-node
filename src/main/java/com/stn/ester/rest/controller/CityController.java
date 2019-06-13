package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.City;
import com.stn.ester.rest.service.CityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController extends AppController<CityService, City> {
    public CityController(CityService cityService) {
        super(cityService);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object getListCity() {
        return service.getListById();
    }
}
