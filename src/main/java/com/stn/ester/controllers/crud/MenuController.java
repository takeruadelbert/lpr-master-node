package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.core.exceptions.UnauthorizedException;
import com.stn.ester.dto.PrivilegeDTO;
import com.stn.ester.entities.Menu;
import com.stn.ester.entities.User;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.services.crud.MenuService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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

    @RequireLogin
    @RequestMapping(value = "/navbar", method = RequestMethod.GET)
    public Collection<Menu> getNavBar() {
        User user = SessionHelper.getCurrentUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        return this.menuService.getByUserGroupId(SessionHelper.getRoleIds());
    }

    @RequireLogin
    @RequestMapping(value = "/check_privilege/{menuId}", method = RequestMethod.GET)
    public PrivilegeDTO checkPrivilege(@PathVariable long menuId) {
        return this.service.checkPrivilege(SessionHelper.getRoleIds(), menuId);
    }

    @RequireLogin
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
