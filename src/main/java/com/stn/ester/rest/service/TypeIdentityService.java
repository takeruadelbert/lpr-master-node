package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.TypeIdentityRepository;
import com.stn.ester.rest.domain.TypeIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeIdentityService extends AppService {

    @Autowired
    public TypeIdentityService(TypeIdentityRepository typeIdentityRepository) {
        super(TypeIdentity.unique_name);
        super.repositories.put(TypeIdentity.unique_name, typeIdentityRepository);
    }
}