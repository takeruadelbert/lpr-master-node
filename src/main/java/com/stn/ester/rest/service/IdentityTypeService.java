package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.IdentityTypeRepository;
import com.stn.ester.rest.domain.IdentityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityTypeService extends AppService {

    @Autowired
    public IdentityTypeService(IdentityTypeRepository typeIdentityRepository) {
        super(IdentityType.unique_name);
        super.repositories.put(IdentityType.unique_name, typeIdentityRepository);
    }
}