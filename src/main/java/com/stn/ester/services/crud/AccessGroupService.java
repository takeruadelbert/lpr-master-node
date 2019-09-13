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

import static com.stn.ester.etc.security.SecurityConstants.ROLE_PREFIX;

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
        super(AccessGroup.class, accessGroupRepository);
        super.repositories.put(AccessGroup.class.getName(), accessGroupRepository);
        super.repositories.put(Menu.class.getName(), menuRepository);
        super.repositories.put(UserGroup.class.getName(), userGroupRepository);
        super.repositories.put(Module.class.getName(), moduleRepository);
        super.repositories.put(ModuleLink.class.getName(), moduleLinkRepository);
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
        List<ModuleLink> moduleLinks = moduleLinkRepository.findAllByRequestMethodAndName(requestMethod, name);
        List<Long> moduleIds = new ArrayList();
        moduleLinks.stream().forEach((ml) -> moduleIds.add(ml.getModule().getId()));
        //find modules
        List<Module> modules = new ArrayList();
        if (isCrud) {
            modules.addAll(moduleRepository.findAllByName(name));
        } else {
            modules.addAll(moduleRepository.findAllByRequestMethodAndName(requestMethod, name));
        }
        Iterables.addAll(modules, moduleRepository.findAllById(moduleIds));
        if (modules.isEmpty()) {
            return "NOACCESS";
        }
        List<Menu> menus = menuRepository.findAllByModuleId(modules.get(0).getId());
        if (menus.isEmpty()) {
            return "NOACCESS";
        }
        Long userGroupId = SessionHelper.getCurrentUser().getUserGroupId();
        Collection<AccessGroup> accessGroups = currentEntityRepository.findAllByMenuIdAndUserGroupId(menus.get(0).getId(), userGroupId);
        if (accessGroups.isEmpty()) {
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
