package com.stn.lprmaster.services;


import com.stn.ester.services.base.CrudService;
import com.stn.lprmaster.entities.InputFrame;
import com.stn.lprmaster.repositories.InputFrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InputFrameService extends CrudService<InputFrame, InputFrameRepository> {
    private InputFrameRepository inputFrameRepository;

    @Autowired
    public InputFrameService(InputFrameRepository inputFrameRepository) {
        super(inputFrameRepository);
        this.inputFrameRepository = inputFrameRepository;
    }
}
