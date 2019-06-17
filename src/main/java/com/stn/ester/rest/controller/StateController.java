package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.State;
import com.stn.ester.rest.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/states")
public class StateController extends AppController<StateService, State> {
    @Autowired
    public StateController(StateService stateService) {
        super(stateService);
    }
}
