package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuService extends AppService {

    private MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        super(Menu.unique_name);
        super.repositories.put(Menu.unique_name, menuRepository);
        this.menuRepository = menuRepository;
    }

    @Override
    public Object get(Long id) {
        if (repositories.get(baseRepoName).existsById(id)) {
            Object o = menuRepository.findById(id).get();
            List<Menu> subMenu = this.findSubMenu(id);

            //Set<Menu> subMenu=menuRepository.findAllByParentMenuId(id);
            ((Menu) o).mergeSubMenu(subMenu);
            return o;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private List<Menu> findSubMenu(Long parentId) {
        List<Menu> subMenu = menuRepository.findAllByParentMenuId(parentId);
        if (!subMenu.isEmpty()) {
            for (Menu menu : subMenu) {
                List<Menu> subMenuIt = this.findSubMenu(menu.getId());
                int n=subMenu.indexOf(menu);
                Menu mergedMenu=subMenu.get(n);
                mergedMenu.mergeSubMenu(subMenuIt);
                subMenu.set(n,mergedMenu);
            }
        }
        return subMenu;
    }
}
