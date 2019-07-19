package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.domain.Module;
import com.stn.ester.rest.domain.UserGroup;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class AccessGroupService extends AppService {

    AccessGroupRepository accessGroupRepository;
    UserGroupRepository userGroupRepository;

    @Autowired
    public AccessGroupService(AccessGroupRepository accessGroupRepository, MenuRepository menuRepository, UserGroupRepository userGroupRepository) {
        super(AccessGroup.unique_name);
        super.repositories.put(AccessGroup.unique_name, accessGroupRepository);
        super.repositories.put(Menu.unique_name, menuRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.accessGroupRepository = accessGroupRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Transactional
    public void updateAll(Long userGroupId, ArrayList<HashMap> accessGroups) {
        for (HashMap<String, Object> it : accessGroups) {
            AccessGroup accessGroup = new AccessGroup();
            accessGroup.setMenuId(Long.parseLong(it.get("menuId").toString()));
            accessGroup.setViewable(Boolean.parseBoolean(it.get("viewable").toString()));
            accessGroup.setAddable(Boolean.parseBoolean(it.get("addable").toString()));
            accessGroup.setEditable(Boolean.parseBoolean(it.get("editable").toString()));
            accessGroup.setDeleteable(Boolean.parseBoolean(it.get("deleteable").toString()));
            accessGroup.setUserGroupId(userGroupId);
            if (it.containsKey("id")) {
                Long accessGroupId = Long.parseLong(it.get("id").toString());
                accessGroup.setId(accessGroupId);
                super.update(accessGroupId, accessGroup);
            } else {
                super.create(accessGroup);
            }
        }
    }

    public Collection<? extends GrantedAuthority> buildAccessAuthorities(Long userGroupId) {
        Collection<GrantedAuthority> authorities = new ArrayList();
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByUserGroupId(userGroupId);
        for (AccessGroup accessGroup : accessGroups) {
            Menu menu = accessGroup.getMenu();
            Module module = menu.getModule();
            if (module != null) {
                if (accessGroup.isViewable())
                    authorities.add(new SimpleGrantedAuthority("ACCESS_" + module.getRequestMethod() + "_" + module.getName()));
                if (accessGroup.isAddable())
                    authorities.add(new SimpleGrantedAuthority("ACCESS_" + RequestMethod.POST + "_" + module.getName()));
                if (accessGroup.isEditable())
                    authorities.add(new SimpleGrantedAuthority("ACCESS_" + RequestMethod.PUT + "_" + module.getName()));
                if (accessGroup.isDeleteable())
                    authorities.add(new SimpleGrantedAuthority("ACCESS_" + RequestMethod.DELETE + "_" + module.getName()));
            }
        }
        return authorities;
    }

}
