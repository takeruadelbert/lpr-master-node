package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.ModuleLinkRepository;
import com.stn.ester.rest.domain.ModuleLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleLinkService extends AppService {

    @Autowired
    public ModuleLinkService(ModuleLinkRepository moduleLinkRepository){
        super(ModuleLink.unique_name);
        super.repositories.put(ModuleLink.unique_name,moduleLinkRepository);
    }
}
