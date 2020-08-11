package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.entities.State;
import com.stn.ester.services.crud.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/states")
public class StateController extends CrudController<StateService, State> {
    @Autowired
    public StateController(StateService stateService) {
        super(stateService);
    }

    @RequireLogin
    @RequestMapping(value = "/list-by-country/{country_id}", method = RequestMethod.GET)
    public Object getStateListByCountry(@PathVariable long country_id) {
        return service.getStateListByCountry(country_id);
    }
}
