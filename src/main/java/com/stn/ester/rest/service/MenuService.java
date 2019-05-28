package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AccessGroupRepository;
import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuService extends AppService {

    private MenuRepository menuRepository;
    private UserGroupService userGroupService;
    private AccessGroupRepository accessGroupRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, UserGroupService userGroupService, AccessGroupRepository accessGroupRepository) {
        super(Menu.unique_name);
        super.repositories.put(Menu.unique_name, menuRepository);
        this.menuRepository = menuRepository;
        this.userGroupService = userGroupService;
        this.accessGroupRepository = accessGroupRepository;
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

}
