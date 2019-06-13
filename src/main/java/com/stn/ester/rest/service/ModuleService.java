package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.ModuleRepository;
import com.stn.ester.rest.domain.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService extends AppService {

    @Autowired
    public ModuleService(ModuleRepository moduleRepository){
        super(Module.unique_name);
        super.repositories.put(Module.unique_name,moduleRepository);
    }

}
