package com.stn.lprmaster.services;

import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.OutputImage;
import com.stn.lprmaster.repositories.OutputImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutputImageService extends CrudService<OutputImage, OutputImageRepository> {
    private OutputImageRepository outputImageRepository;

    @Autowired
    public OutputImageService(OutputImageRepository outputImageRepository) {
        super(outputImageRepository);
        this.outputImageRepository = outputImageRepository;
    }
}
