package com.stn.lprmaster.controllers;

import com.stn.ester.controllers.base.CrudController;
import com.stn.lprmaster.entities.OutputImage;
import com.stn.lprmaster.services.OutputImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/output-image")
public class OutputImageController extends CrudController<OutputImageService, OutputImage> {
    public OutputImageController(OutputImageService outputImageService) {
        super(outputImageService);
    }
}
