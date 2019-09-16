package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Menu;
import com.stn.ester.entities.User;
import com.stn.ester.core.exceptions.UnauthorizedException;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.services.crud.MenuService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("menus")
public class MenuController extends CrudController<MenuService, Menu> {

    private UserService userService;
    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService, UserService userService) {
        super(menuService);
        this.userService = userService;
        this.menuService = menuService;
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/navbar", method = RequestMethod.GET)
    public Object getAccessGroup() {
        User user = SessionHelper.getCurrentUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        return this.menuService.getByUserGroupId(user.getUserGroupId());
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/check_privilege/{menuId}", method = RequestMethod.GET)
    public Object checkPrivilege(@PathVariable long menuId) {
        return this.service.checkPrivilege(SessionHelper.getUserGroupId(), menuId);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object viewMenuSubMenu() {
        return this.menuService.getAllMenuSubmenu();
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody Menu menu) {
        return service.create(menu);
    }

}
