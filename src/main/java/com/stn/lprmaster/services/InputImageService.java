package com.stn.lprmaster.services;

import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.InputImage;
import com.stn.lprmaster.entities.enumerate.InputImageStatus;
import com.stn.lprmaster.repositories.InputImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InputImageService extends CrudService<InputImage, InputImageRepository> {
    private InputImageRepository inputImageRepository;

    @Autowired
    public InputImageService(InputImageRepository inputImageRepository) {
        super(inputImageRepository);
        this.inputImageRepository = inputImageRepository;
    }

    public Map<InputImageStatus, String> getInputImageStatus() {
        return InputImageStatus.toList().stream().collect(Collectors.toMap(status -> status, InputImageStatus::getLabel));
    }
}
