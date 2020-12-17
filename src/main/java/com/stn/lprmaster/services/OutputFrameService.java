package com.stn.lprmaster.services;

import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.OutputFrame;
import com.stn.lprmaster.repositories.OutputFrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutputFrameService extends CrudService<OutputFrame, OutputFrameRepository> {
    private OutputFrameRepository outputFrameRepository;

    @Autowired
    public OutputFrameService(OutputFrameRepository outputFrameRepository) {
        super(outputFrameRepository);
        this.outputFrameRepository = outputFrameRepository;
    }
}
