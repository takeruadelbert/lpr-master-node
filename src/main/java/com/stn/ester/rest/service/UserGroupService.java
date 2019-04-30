package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends AppService {

    @Autowired
    public UserGroupService(UserGroupRepository usergroupRepository) {
        super(UserGroup.unique_name);
        super.repositories.put(UserGroup.unique_name, usergroupRepository);
    }

}
