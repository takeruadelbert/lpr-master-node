package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.City;
import com.stn.ester.rest.domain.enumerate.CityStatus;
import com.stn.ester.rest.service.CityService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/list-by-state/{state_id}", method = RequestMethod.GET)
    public Object getCityListByState(@PathVariable long state_id) {
        return service.getCityListByState(state_id);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/city_status", method = RequestMethod.OPTIONS)
    public Map<CityStatus,String> getCityStatusList() {
        return service.getCityStatusList();
    }
}
