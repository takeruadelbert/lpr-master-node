package com.stn.lprmaster.controllers;

import com.stn.ester.controllers.base.CrudController;
import com.stn.lprmaster.entities.OutputFrame;
import com.stn.lprmaster.services.OutputFrameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/output-frame")
public class OutputFrameController extends CrudController<OutputFrameService, OutputFrame> {
    public OutputFrameController(OutputFrameService outputFrameService) {
        super(outputFrameService);
    }
}
