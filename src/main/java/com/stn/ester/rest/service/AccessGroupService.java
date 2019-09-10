package com.stn.ester.rest.service;

import com.google.common.collect.Iterables;
import com.stn.ester.rest.dao.jpa.*;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.domain.Module;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.helper.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.stn.ester.rest.security.SecurityConstants.ROLE_PREFIX;

@Service
public class AccessGroupService extends AppService {

    AccessGroupRepository accessGroupRepository;
    UserGroupRepository userGroupRepository;
    ModuleRepository moduleRepository;
    MenuRepository menuRepository;
    ModuleLinkRepository moduleLinkRepository;

    @Autowired
    public AccessGroupService(AccessGroupRepository accessGroupRepository,
                              MenuRepository menuRepository,
                              UserGroupRepository userGroupRepository,
                              ModuleRepository moduleRepository,
                              ModuleLinkRepository moduleLinkRepository) {
        super(AccessGroup.unique_name);
        super.repositories.put(AccessGroup.unique_name, accessGroupRepository);
        super.repositories.put(Menu.unique_name, menuRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.accessGroupRepository = accessGroupRepository;
        this.userGroupRepository = userGroupRepository;
        this.moduleRepository = moduleRepository;
        this.menuRepository = menuRepository;
        this.moduleLinkRepository = moduleLinkRepository;
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

    public String findAccessRole(RequestMethod requestMethod, String name, Boolean isCrud) {
        //find module links
        List<ModuleLink> moduleLinks = this.moduleLinkRepository.findAllByRequestMethodAndName(requestMethod, name);
        List<Long> moduleIds = new ArrayList();
        moduleLinks.stream().forEach((ml) -> moduleIds.add(ml.getModule().getId()));
        //find modules
        List<Module> modules = new ArrayList();
        if (isCrud) {
            System.out.println("is CRUD");
            modules.addAll(this.moduleRepository.findAllByName(name));
        } else {
            modules.addAll(this.moduleRepository.findAllByRequestMethodAndName(requestMethod, name));
        }
        Iterables.addAll(modules, this.moduleRepository.findAllById(moduleIds));
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
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByMenuIdAndUserGroupId(menus.get(0).getId(), userGroupId);
        if (accessGroups.isEmpty()) {
            System.out.println("access group not found");
            return "NOACCESS";
        }
        if (!this.hasAccess(requestMethod, Iterables.get(accessGroups, 0), modules.get(0), isCrud))
            return "NOACCESS";
        return ROLE_PREFIX + "_" + SessionHelper.getCurrentUser().getUserGroup().getName();
    }

    private boolean hasAccess(RequestMethod requestMethod, AccessGroup accessGroup, Module module, Boolean isCrud) {
        if (isCrud) {
            if (accessGroup.isViewable() && (requestMethod.equals(RequestMethod.GET) || requestMethod.equals(RequestMethod.OPTIONS))) {
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
        } else if (accessGroup.isViewable() && module.getRequestMethod().equals(requestMethod)) {
            return true;
        }
        return false;
    }

}
