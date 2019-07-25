package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Position;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/positions")
public class PositionController extends AppController<PositionService, Position> {

    @Autowired
    public PositionController(PositionService positionService) {
        super(positionService);
    }
}
