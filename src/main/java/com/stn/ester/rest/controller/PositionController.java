package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Position;
import com.stn.ester.rest.service.PositionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/positions")
public class PositionController extends AppController<PositionService, Position> {
    public PositionController(PositionService positionService) {
        super(positionService);
    }
}
