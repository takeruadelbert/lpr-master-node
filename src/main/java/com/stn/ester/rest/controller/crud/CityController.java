package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.City;
import com.stn.ester.rest.domain.enumerate.CityStatus;
import com.stn.ester.rest.service.CityService;
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

    @RequestMapping(value = "/list-by-state/{state_id}", method = RequestMethod.GET)
    public Object getCityListByState(@PathVariable long state_id) {
        return service.getCityListByState(state_id);
    }

    @RequestMapping(value = "/city_status/list", method = RequestMethod.GET)
    public Map<CityStatus,String> getCityStatusList() {
        return service.getCityStatusList();
    }
}
