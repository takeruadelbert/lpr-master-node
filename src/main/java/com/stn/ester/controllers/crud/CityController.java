package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.entities.City;
import com.stn.ester.entities.enumerate.CityStatus;
import com.stn.ester.services.crud.CityService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cities")
public class CityController extends CrudController<CityService, City> {
    public CityController(CityService cityService) {
        super(cityService);
    }

    @RequireLogin
    @RequestMapping(value = "/list-by-state/{state_id}", method = RequestMethod.GET)
    public Object getCityListByState(@PathVariable long state_id) {
        return service.getCityListByState(state_id);
    }

    @RequireLogin
    @RequestMapping(value = "/city_status", method = RequestMethod.OPTIONS)
    public Map<CityStatus,String> getCityStatusList() {
        return service.getCityStatusList();
    }
}
