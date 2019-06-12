package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.exception.UnauthorizedException;
import com.stn.ester.rest.service.MenuService;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("menus")
public class MenuController extends AppController<MenuService, Menu> {

    private UserService userService;
    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService, UserService userService) {
        super(menuService);
        this.userService=userService;
        this.menuService=menuService;
    }

    @RequestMapping(value ="/navbar", method = RequestMethod.GET)
    public Object getAccessGroup(@RequestHeader("access-token") String accessToken){
        LoginSession loginSession=this.userService.isValidToken(accessToken);
        if (accessToken==null || loginSession==null){
            throw new UnauthorizedException();
        }
        Long userGroupId=loginSession.getUser().getUserGroupId();
        return this.menuService.getByUserGroupId(userGroupId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object viewMenuSubMenu() {
        return this.menuService.getAllMenuSubmenu();
    }
}
