package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Position;
import com.stn.ester.services.crud.PositionService;
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
