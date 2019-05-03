package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessGroupService extends AppService {

    @Autowired
    public AccessGroupService(AccessGroupRepository accessGroupRepository) {
        super(AccessGroup.unique_name);
        super.repositories.put(AccessGroup.unique_name, accessGroupRepository);
    }

}
