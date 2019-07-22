package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.Position;
import com.stn.ester.rest.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/positions")
public class PositionController extends CrudController<PositionService, Position> {

    @Autowired
    public PositionController(PositionService positionService) {
        super(positionService);
    }
}
