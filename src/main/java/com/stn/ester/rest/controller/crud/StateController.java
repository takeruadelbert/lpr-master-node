package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.State;
import com.stn.ester.rest.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/states")
public class StateController extends CrudController<StateService, State> {
    @Autowired
    public StateController(StateService stateService) {
        super(stateService);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/list-by-country/{country_id}", method = RequestMethod.GET)
    public Object getStateListByCountry(@PathVariable long country_id) {
        return service.getStateListByCountry(country_id);
    }
}
