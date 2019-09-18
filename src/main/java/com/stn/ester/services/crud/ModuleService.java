package com.stn.ester.services.crud;

import com.stn.ester.entities.Module;
import com.stn.ester.repositories.jpa.ModuleRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService extends CrudService {

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        super(moduleRepository);
    }

}
