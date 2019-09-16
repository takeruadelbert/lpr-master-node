package com.stn.ester.services.crud;

import com.stn.ester.entities.IdentityType;
import com.stn.ester.repositories.jpa.IdentityTypeRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityTypeService extends CrudService {

    @Autowired
    public IdentityTypeService(IdentityTypeRepository typeIdentityRepository) {
        super(typeIdentityRepository);
    }
}