package com.stn.ester.services.crud;

import com.google.common.collect.Iterables;
import com.stn.ester.entities.*;
import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.repositories.jpa.*;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.stn.ester.core.security.SecurityConstants.ROLE_PREFIX;

@Service
public class AccessGroupService extends CrudService<AccessGroup, AccessGroupRepository> {

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
        super(accessGroupRepository);
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
        Collection<AccessGroup> accessGroups = currentEntityRepository.findAllByUserGroupId(userGroupId);
        for (AccessGroup accessGroup : accessGroups) {
            Menu menu = accessGroup.getMenu();
            com.stn.ester.entities.Module module = menu.getModule();
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
        List<ModuleLink> moduleLinks = moduleLinkRepository.findAllByRequestMethodAndName(requestMethod, name);
        List<Long> moduleIds = new ArrayList();
        moduleLinks.stream().forEach((ml) -> moduleIds.add(ml.getModule().getId()));
        //find modules
        List<com.stn.ester.entities.Module> modules = new ArrayList();
        if (isCrud) {
            modules.addAll(moduleRepository.findAllByName(name));
        } else {
            modules.addAll(moduleRepository.findAllByRequestMethodAndName(requestMethod, name));
        }
        Iterables.addAll(modules, moduleRepository.findAllById(moduleIds));
        if (modules.isEmpty()) {
            return "NOACCESS";
        }
        List<Menu> menus = menuRepository.findAllByModuleIdIn(modules.stream().map(com.stn.ester.entities.Module::getId).collect(Collectors.toCollection(ArrayList::new)));
        if (menus.isEmpty()) {
            return "NOACCESS";
        }
        Long userGroupId = SessionHelper.getCurrentUser().getUserGroupId();
        Collection<AccessGroup> accessGroups = currentEntityRepository.findAllByMenuIdInAndUserGroupId(menus.stream().map(Menu::getId).collect(Collectors.toCollection(ArrayList::new)), userGroupId);
        if (accessGroups.isEmpty()) {
            return "NOACCESS";
        }
        if (!this.hasAccess(requestMethod, accessGroups, name, isCrud))
            return "NOACCESS";
        return ROLE_PREFIX + "_" + SessionHelper.getCurrentUser().getUserGroup().getName();
    }

    private boolean hasAccess(RequestMethod requestMethod, Collection<AccessGroup> accessGroups, String moduleName, Boolean isCrud) {
        boolean result = false;
        for (AccessGroup accessGroup : accessGroups) {
            if (isCrud) {
                if (accessGroup.isViewable() && (requestMethod.equals(RequestMethod.GET) || requestMethod.equals(RequestMethod.OPTIONS))) {
                    result |= true;
                }
                if (accessGroup.isAddable() && requestMethod.equals(RequestMethod.POST)) {
                    result |= true;
                }
                if (accessGroup.isEditable() && requestMethod.equals(RequestMethod.PUT)) {
                    result |= true;
                }
                if (accessGroup.isDeleteable() && requestMethod.equals(RequestMethod.DELETE)) {
                    result |= true;
                }
            } else if (accessGroup.isViewable() && accessGroup.getMenu().getModule().getRequestMethod().equals(requestMethod)) {
                result |= true;
            }
            Set<ModuleLink> moduleLinks = accessGroup.getMenu().getModule().getModuleLink();
            for (ModuleLink moduleLink : moduleLinks) {
                if (accessGroup.isViewable() && (moduleLink.getName().equals(moduleName) && moduleLink.getRequestMethod().equals(requestMethod))) {
                    result |= true;
                }
            }
        }
        return result;
    }

}
