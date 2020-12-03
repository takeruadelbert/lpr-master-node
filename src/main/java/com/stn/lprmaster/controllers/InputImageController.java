package com.stn.lprmaster.controllers;

import com.stn.ester.controllers.base.CrudController;
import com.stn.lprmaster.entities.InputImage;
import com.stn.lprmaster.entities.enumerate.InputImageStatus;
import com.stn.lprmaster.services.InputImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/input-image")
public class InputImageController extends CrudController<InputImageService, InputImage> {
    public InputImageController(InputImageService inputImageService) {
        super(inputImageService);
    }

    @GetMapping("/status")
    public Map<InputImageStatus, String> getInputImageStatus() {
        return service.getInputImageStatus();
    }
}
