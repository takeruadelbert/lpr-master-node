package com.stn.ester.services.crud;

import com.stn.ester.entities.ModuleLink;
import com.stn.ester.repositories.jpa.ModuleLinkRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleLinkService extends CrudService {

    @Autowired
    public ModuleLinkService(ModuleLinkRepository moduleLinkRepository) {
        super(moduleLinkRepository);
    }
}
