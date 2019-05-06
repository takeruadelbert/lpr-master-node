package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserGroupService extends AppService {

    private UserGroupRepository userGroupRepository;
    private AccessGroupRepository accessGroupRepository;

    @Autowired
    public UserGroupService(UserGroupRepository usergroupRepository, AccessGroupRepository accessGroupRepository) {
        super(UserGroup.unique_name);
        super.repositories.put(UserGroup.unique_name, usergroupRepository);
        this.userGroupRepository=usergroupRepository;
        this.accessGroupRepository=accessGroupRepository;
    }

    public UserGroup getAccessGroup(Long id){
        if (!this.userGroupRepository.existsById(id))
            throw new ResourceNotFoundException();
        UserGroup userGroup = this.userGroupRepository.findById(id).get();
        Set<AccessGroup> accessGroups=this.accessGroupRepository.findAllByUserGroupId(id);
        userGroup.mergeAccessGroup(accessGroups);
        return userGroup;
    }

}
