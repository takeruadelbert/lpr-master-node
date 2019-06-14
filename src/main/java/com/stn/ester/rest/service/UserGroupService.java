package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.domain.UserGroup;
import org.modelmapper.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserGroupService extends AppService {

    private UserGroupRepository userGroupRepository;
    private AccessGroupRepository accessGroupRepository;
    private MenuRepository menuRepository;

    @Autowired
    public UserGroupService(UserGroupRepository usergroupRepository, AccessGroupRepository accessGroupRepository, MenuRepository menuRepository) {
        super(UserGroup.unique_name);
        super.repositories.put(UserGroup.unique_name, usergroupRepository);
        this.userGroupRepository = usergroupRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.menuRepository = menuRepository;
    }

    public Long getIdByName(String name) {
        UserGroup userGroup = userGroupRepository.findByName(name);
        if (userGroup != null)
            return userGroup.getId();
        return null;
    }

    public UserGroup getAccessGroup(Long id) {
        if (!this.userGroupRepository.existsById(id))
            throw new ResourceNotFoundException();
        UserGroup userGroup = this.userGroupRepository.findById(id).get();
        Set<AccessGroup> accessGroups = this.accessGroupRepository.findAllByUserGroupId(id);
        userGroup.mergeAccessGroup(accessGroups);
        return userGroup;
    }

    @Override
    @Transactional
    public Object create(AppDomain o) {
        UserGroup userGroup = (UserGroup) super.create(o);
        Long lastInsertID = userGroup.getId();

        // add all menus & submenus once user group has been added (if there's any)
        Iterable<Menu> menus = this.menuRepository.findAll();
        List<AccessGroup> accessGroups = new ArrayList<>();
        for (Menu menu : menus) {
            AccessGroup accessGroup = new AccessGroup(lastInsertID, menu.getId(), true, false, false, false);
            accessGroups.add(accessGroup);
        }
        this.accessGroupRepository.saveAll(accessGroups);
        userGroup.mergeAccessGroup(new HashSet<>(accessGroups));
        return userGroup;
    }
}
