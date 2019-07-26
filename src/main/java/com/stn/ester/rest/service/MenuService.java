package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.domain.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MenuService extends AppService {

    private MenuRepository menuRepository;
    private UserGroupService userGroupService;
    private AccessGroupRepository accessGroupRepository;
    private UserGroupRepository userGroupRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, UserGroupService userGroupService, AccessGroupRepository accessGroupRepository, UserGroupRepository userGroupRepository) {
        super(Menu.unique_name);
        super.repositories.put(Menu.unique_name, menuRepository);
        this.menuRepository = menuRepository;
        this.userGroupService = userGroupService;
        this.accessGroupRepository = accessGroupRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Object get(Long id) {
        if (repositories.get(baseRepoName).existsById(id)) {
            Object o = menuRepository.findById(id).get();
            List<Menu> subMenus = this.findSubMenu(id);
            ((Menu) o).mergeSubMenu(subMenus);
            return o;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Object getByUserGroupId(Long userGroupId) {
        Set<AccessGroup> accessGroups = this.accessGroupRepository.findAllByUserGroupIdAndViewable(userGroupId, true);
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

    public Object getByUserGroupName(String userGroupName) {
        return this.getByUserGroupId(this.userGroupService.getIdByName(userGroupName));
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
    public Object create(AppDomain o) {
        Menu menu = (Menu) super.create(o);
        Long lastInsertID = menu.getId();

        // automatically add access group once either menu or submenu has been added
        Iterable<UserGroup> userGroups = this.userGroupRepository.findAll();
        List<AccessGroup> accessGroups = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            AccessGroup accessGroup = new AccessGroup(userGroup.getId(), lastInsertID, true, false, false, false);
            accessGroups.add(accessGroup);
        }
        this.accessGroupRepository.saveAll(accessGroups);
        return menu;
    }

    @Override
    public void delete(Long id) {
        // delete access group as well
        Iterable<AccessGroup> accessGroups = this.accessGroupRepository.findAllByMenuId(id);
        this.accessGroupRepository.deleteAll(accessGroups);

        super.delete(id);
    }
}
