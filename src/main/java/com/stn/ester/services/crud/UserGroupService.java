package com.stn.ester.services.crud;

import com.stn.ester.entities.AccessGroup;
import com.stn.ester.entities.Menu;
import com.stn.ester.entities.UserGroup;
import com.stn.ester.core.security.SecurityConstants;
import com.stn.ester.repositories.jpa.AccessGroupRepository;
import com.stn.ester.repositories.jpa.MenuRepository;
import com.stn.ester.repositories.jpa.UserGroupRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserGroupService extends CrudService<UserGroup, UserGroupRepository> {

    private UserGroupRepository userGroupRepository;
    private AccessGroupRepository accessGroupRepository;
    private MenuRepository menuRepository;

    @Autowired
    public UserGroupService(UserGroupRepository usergroupRepository, AccessGroupRepository accessGroupRepository, MenuRepository menuRepository) {
        super(usergroupRepository);
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
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByUserGroupId(id);
        userGroup.mergeAccessGroup(accessGroups);
        return userGroup;
    }

    @Override
    @Transactional
    public UserGroup create(UserGroup o) {
        UserGroup userGroup = super.create(o);
        Long lastInsertID = userGroup.getId();

        // add all menus & submenus once user group has been added (if there's any)
        Iterable<Menu> menus = this.menuRepository.findAll();
        List<AccessGroup> accessGroups = new ArrayList<>();
        for (Menu menu : menus) {
            AccessGroup accessGroup;
            if (userGroup.getName().equals(SecurityConstants.ROLE_SUPERADMIN)) {
                accessGroup = new AccessGroup(lastInsertID, menu.getId(), true, true, true, true);
            } else {
                accessGroup = new AccessGroup(lastInsertID, menu.getId(), false, false, false, false);
            }
            accessGroups.add(accessGroup);
        }
        this.accessGroupRepository.saveAll(accessGroups);
        userGroup.mergeAccessGroup(new HashSet<>(accessGroups));
        return userGroup;
    }

    @Override
    public void delete(Long id) {
        // delete access group as well
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByUserGroupId(id);
        this.accessGroupRepository.deleteAll(accessGroups);
        super.delete(id);
    }

    @Transactional
    public Object createIfNameNotExist(UserGroup userGroup, String name) {
        UserGroup currentUserGroup = this.userGroupRepository.findByName(name);
        if (currentUserGroup == null) {
            return this.create(userGroup);
        }
        return currentUserGroup;
    }
}
