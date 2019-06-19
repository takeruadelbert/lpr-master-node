package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.State;
import com.stn.ester.rest.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/states")
public class StateController extends AppController<StateService, State> {
    @Autowired
    public StateController(StateService stateService) {
        super(stateService);
    }

    @RequestMapping(value = "/list-by-country/{country_id}", method = RequestMethod.GET)
    public Object getStateListByCountry(@PathVariable long country_id) {
        return service.getStateListByCountry(country_id);
    }
}
