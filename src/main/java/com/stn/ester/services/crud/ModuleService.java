package com.stn.ester.services.crud;

import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.repositories.jpa.ModuleRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class ModuleService extends CrudService {

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        super(moduleRepository);
    }

    public Collection<RequestMethod> listRequestMethod() {
        Collection<RequestMethod> requestMethods = Arrays.asList(RequestMethod.values());
        return requestMethods;
    }

}
