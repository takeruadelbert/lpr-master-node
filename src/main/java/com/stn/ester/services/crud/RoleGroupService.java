package com.stn.ester.services.crud;

import com.stn.ester.repositories.jpa.RoleGroupRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleGroupService extends CrudService {

    @Autowired
    public RoleGroupService(RoleGroupRepository roleGroupRepository) {
        super(roleGroupRepository);
    }
}
