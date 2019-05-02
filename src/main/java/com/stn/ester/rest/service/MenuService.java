package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.MenuRepository;
import com.stn.ester.rest.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends AppService {

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        super(Menu.unique_name);
        super.repositories.put(Menu.unique_name, menuRepository);
    }

}
