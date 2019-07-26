package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.dao.jpa.ModuleRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.domain.Module;
import com.stn.ester.rest.domain.UserGroup;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.stn.ester.rest.security.SecurityConstants.ROLE_PREFIX;

@Service
public class AccessGroupService extends AppService {

    AccessGroupRepository accessGroupRepository;
    UserGroupRepository userGroupRepository;
    ModuleRepository moduleRepository;
    MenuRepository menuRepository;

    @Autowired
    public AccessGroupService(AccessGroupRepository accessGroupRepository,
                              MenuRepository menuRepository,
                              UserGroupRepository userGroupRepository,
                              ModuleRepository moduleRepository) {
        super(AccessGroup.unique_name);
        super.repositories.put(AccessGroup.unique_name, accessGroupRepository);
        super.repositories.put(Menu.unique_name, menuRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.accessGroupRepository = accessGroupRepository;
        this.userGroupRepository = userGroupRepository;
        this.moduleRepository = moduleRepository;
        this.menuRepository = menuRepository;
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

    public String findAccessRole(RequestMethod requestMethod, String name) {
        List<Module> modules = this.moduleRepository.findAllByName(name);
        System.out.println("find module : " + requestMethod + " " + name);
        if (modules.isEmpty()) {
            System.out.println("module not found");
            return "NOACCESS";
        }
        List<Menu> menus = this.menuRepository.findAllByModuleId(modules.get(0).getId());
        if (menus.isEmpty()) {
            System.out.println("menu not found");
            return "NOACCESS";
        }
        Long userGroupId = SessionHelper.getCurrentUser().getUserGroupId();
        List<AccessGroup> accessGroups = this.accessGroupRepository.findAllByMenuIdAndUserGroupId(menus.get(0).getId(), userGroupId);
        if (accessGroups.isEmpty()) {
            System.out.println("access group not found");
            return "NOACCESS";
        }
        if (!this.hasAccess(requestMethod, accessGroups.get(0),modules.get(0)))
            return "NOACCESS";
        return ROLE_PREFIX + "_" + SessionHelper.getCurrentUser().getUserGroup().getName();
    }

    private boolean hasAccess(RequestMethod requestMethod, AccessGroup accessGroup,Module module) {
        if (accessGroup.isViewable() && requestMethod.equals(RequestMethod.GET)) {
            return true;
        }
        if (accessGroup.isAddable() && requestMethod.equals(RequestMethod.POST)) {
            return true;
        }
        if (accessGroup.isEditable() && requestMethod.equals(RequestMethod.PUT)) {
            return true;
        }
        if (accessGroup.isDeleteable() && requestMethod.equals(RequestMethod.DELETE)) {
            return true;
        }
        if (module.getRequestMethod().equals(requestMethod)){
            return true;
        }
        return false;
    }

}
