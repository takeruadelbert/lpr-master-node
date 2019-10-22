package com.stn.ester.services.crud;

import com.stn.ester.entities.AccessGroup;
import com.stn.ester.entities.Menu;
import com.stn.ester.entities.Role;
import com.stn.ester.core.security.SecurityConstants;
import com.stn.ester.repositories.jpa.AccessGroupRepository;
import com.stn.ester.repositories.jpa.MenuRepository;
import com.stn.ester.repositories.jpa.RoleRepository;
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
public class RoleService extends CrudService<Role, RoleRepository> {

    private RoleRepository roleRepository;
    private AccessGroupRepository accessGroupRepository;
    private MenuRepository menuRepository;

    @Autowired
    public RoleService(RoleRepository usergroupRepository, AccessGroupRepository accessGroupRepository, MenuRepository menuRepository) {
        super(usergroupRepository);
        this.roleRepository = usergroupRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.menuRepository = menuRepository;
    }

    public Long getIdByName(String name) {
        Role role = roleRepository.findByName(name);
        if (role != null)
            return role.getId();
        return null;
    }

    public Role getAccessGroup(Long id) {
        if (!this.roleRepository.existsById(id))
            throw new ResourceNotFoundException();
        Role role = this.roleRepository.findById(id).get();
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByRoleId(id);
        role.mergeAccessGroup(accessGroups);
        return role;
    }

    @Override
    @Transactional
    public Role create(Role o) {
        Role role = super.create(o);
        Long lastInsertID = role.getId();

        // add all menus & submenus once user group has been added (if there's any)
        Iterable<Menu> menus = this.menuRepository.findAll();
        List<AccessGroup> accessGroups = new ArrayList<>();
        for (Menu menu : menus) {
            AccessGroup accessGroup;
            if (role.getName().equals(SecurityConstants.ROLE_SUPERADMIN)) {
                accessGroup = new AccessGroup(lastInsertID, menu.getId(), true, true, true, true);
            } else {
                accessGroup = new AccessGroup(lastInsertID, menu.getId(), false, false, false, false);
            }
            accessGroups.add(accessGroup);
        }
        this.accessGroupRepository.saveAll(accessGroups);
        role.mergeAccessGroup(new HashSet<>(accessGroups));
        return role;
    }

    @Override
    public void delete(Long id) {
        // delete access group as well
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByRoleId(id);
        this.accessGroupRepository.deleteAll(accessGroups);
        super.delete(id);
    }

    @Transactional
    public Object createIfNameNotExist(Role role, String name) {
        Role currentRole = this.roleRepository.findByName(name);
        if (currentRole == null) {
            return this.create(role);
        }
        return currentRole;
    }
}
