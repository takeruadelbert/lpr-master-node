package com.stn.ester.services.crud;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.core.exceptions.NotFoundException;
import com.stn.ester.core.security.SecurityConstants;
import com.stn.ester.dto.PrivilegeDTO;
import com.stn.ester.dto.entity.MenuSimpleDTO;
import com.stn.ester.entities.*;
import com.stn.ester.repositories.jpa.*;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.SimpleSearchTrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService extends CrudService<Menu, MenuRepository> implements SimpleSearchTrait<Menu, MenuSimpleDTO, MenuRepository> {

    private MenuRepository menuRepository;
    private RoleService roleService;
    private AccessGroupRepository accessGroupRepository;
    private RoleRepository roleRepository;
    private ModuleRepository moduleRepository;
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository,
                       RoleService roleService,
                       AccessGroupRepository accessGroupRepository,
                       RoleRepository roleRepository,
                       ModuleRepository moduleRepository,
                       RoleGroupRepository roleGroupRepository
    ) {
        super(menuRepository);
        this.menuRepository = menuRepository;
        this.roleService = roleService;
        this.accessGroupRepository = accessGroupRepository;
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
        this.roleGroupRepository = roleGroupRepository;
    }

    @Override
    public Menu get(Long id) {
        if (currentEntityRepository.existsById(id)) {
            Menu o = menuRepository.findById(id).get();
            List<Menu> subMenus = this.findSubMenu(id);
            o.mergeSubMenu(subMenus);
            return o;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Collection<Menu> getByUserGroupId(Collection<Long> userGroupIds) {
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByRoleIdInAndViewable(userGroupIds, true);
        Set<Long> menuIds = new LinkedHashSet<>();
        for (AccessGroup accessGroup : accessGroups) {
            menuIds.add(accessGroup.getMenuId());
        }
        List<Menu> menus;
        menus = this.menuRepository.findAllByIdInAndParentMenuIdOrderByOrderingNumber(menuIds, null);
        //this.menuRepository.findAllById(menuIds).iterator().forEachRemaining(menus::add);
        for (Menu menu : menus) {
            if (menu.getParentMenuId() != null)
                continue;
            List<Menu> subMenus = this.findSubMenu(menu.getId(), menuIds);
            ((Menu) menu).mergeSubMenu(subMenus);
        }
        return menus;
    }

    private List<Menu> findSubMenu(Long parentId) {
        return this.findSubMenu(parentId, null);
    }

    private List<Menu> findSubMenu(Long parentId, Set<Long> menuIds) {
        List<Menu> subMenus = menuRepository.findAllByParentMenuId(parentId);
        if (!subMenus.isEmpty()) {
            Iterator<Menu> subMenuIterator = subMenus.iterator();
            while (subMenuIterator.hasNext()) {
                Menu menu = subMenuIterator.next();
                if (menuIds != null && !menuIds.contains(menu.getId())) {
                    subMenuIterator.remove();
                    continue;
                }
                List<Menu> subMenuIt = this.findSubMenu(menu.getId(), menuIds);
                int n = subMenus.indexOf(menu);
                Menu mergedMenu = subMenus.get(n);
                mergedMenu.mergeSubMenu(subMenuIt);
                subMenus.set(n, mergedMenu);
            }
        }
        return subMenus;
    }

    public Object getAllMenuSubmenu() {
        List<Menu> menus = menuRepository.findAllByParentMenuIdIsNull();
        List<Object> result = new ArrayList();
        if (!menus.isEmpty()) {
            for (Menu menu : menus) {
                result.add(this.get(menu.getId()));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public Menu create(Menu o) {
        if (o.getModuleId() != null && !moduleRepository.existsById(o.getModuleId())) {
            throw new BadRequestException(String.format("Module id %d not found", o.getModuleId()));
        }
        Menu menu = super.create(o);
        Long lastInsertID = menu.getId();

        // automatically add access group once either menu or submenu has been added
        Iterable<Role> userGroups = this.roleRepository.findAll();
        List<AccessGroup> accessGroups = new ArrayList<>();
        for (Role role : userGroups) {
            AccessGroup accessGroup;
            if (role.getName().equals(SecurityConstants.ROLE_SUPERADMIN)) {
                accessGroup = new AccessGroup(role.getId(), lastInsertID, true, true, true, true);
            } else {
                accessGroup = new AccessGroup(role.getId(), lastInsertID, false, false, false, false);
            }
            accessGroups.add(accessGroup);
        }
        this.accessGroupRepository.saveAll(accessGroups);
        return menu;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // delete access group as well
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByMenuId(id);
        this.accessGroupRepository.deleteAll(accessGroups);

        super.delete(id);
    }

    public PrivilegeDTO checkPrivilege(Collection<Long> userGroupIds, Long menuId) {
        Collection<AccessGroup> accessGroups = this.accessGroupRepository.findAllByMenuIdAndRoleIdIn(menuId, userGroupIds);
        if (accessGroups.isEmpty()) {
            throw new NotFoundException();
        }
        return new PrivilegeDTO(accessGroups);
    }

    @Override
    public Collection<String> getSimpleSearchKeys() {
        List<String> keys = new ArrayList<>();
        keys.add("label");
        keys.add("module.name");
        return keys;
    }

    @Override
    public MenuRepository getRepository() {
        return currentEntityRepository;
    }

    public Collection<User> getUserWhoHaveAccessTo(Menu... menus) {
        List<Long> menuIds = Arrays.asList(menus).stream().map(Menu::getId).collect(Collectors.toList());
        Collection<AccessGroup> accessGroups = accessGroupRepository.findAllByMenuIdInAndViewable(menuIds, true);
        Collection<Long> roleIds = accessGroups.stream().map(o -> o.getRoleId()).collect(Collectors.toList());
        Collection<RoleGroup> roleGroups = roleGroupRepository.findAllByRoleIdIn(roleIds);
        Collection<User> users = roleGroups.stream().map(RoleGroup::getUser).collect(Collectors.toList());
        return users;
    }
}
