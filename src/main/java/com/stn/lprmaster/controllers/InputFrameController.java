package com.stn.lprmaster.controllers;

import com.stn.ester.controllers.base.CrudController;
import com.stn.lprmaster.entities.InputFrame;
import com.stn.lprmaster.services.InputFrameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/input-frame")
public class InputFrameController extends CrudController<InputFrameService, InputFrame> {
    public InputFrameController(InputFrameService inputFrameService) {
        super(inputFrameService);
    }
}
