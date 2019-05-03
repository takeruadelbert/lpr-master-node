package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Menu;
import com.stn.ester.rest.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("menus")
public class MenuController extends AppController<MenuService, Menu> {

    @Autowired
    public MenuController(MenuService menuService) {
        super(menuService);
    }

}
