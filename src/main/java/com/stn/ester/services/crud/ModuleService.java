package com.stn.ester.services.crud;

import com.stn.ester.entities.Menu;
import com.stn.ester.entities.Module;
import com.stn.ester.entities.User;
import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.repositories.jpa.MenuRepository;
import com.stn.ester.repositories.jpa.ModuleRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ModuleService extends CrudService<Module, ModuleRepository> {

    private MenuRepository menuRepository;
    private MenuService menuService;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository,
                         MenuRepository menuRepository,
                         MenuService menuService
    ) {
        super(moduleRepository);
        this.menuRepository = menuRepository;
        this.menuService = menuService;
    }

    public Collection<RequestMethod> listRequestMethod() {
        Collection<RequestMethod> requestMethods = Arrays.asList(RequestMethod.values());
        return requestMethods;
    }

    public Collection<User> getUserWhoHaveAccessTo(String... moduleName) {
        Collection<Module> modules = currentEntityRepository.findAllByNameIn(Arrays.asList(moduleName));
        return getUserWhoHaveAccessTo(modules.toArray(new Module[modules.size()]));
    }

    public Collection<User> getUserWhoHaveAccessTo(Module... modules) {
        Collection<Long> moduleIds = Arrays.asList(modules).stream().map(Module::getId).collect(Collectors.toList());
        Collection<Menu> menus = menuRepository.findAllByModuleIdIn(moduleIds);
        return menuService.getUserWhoHaveAccessTo(menus.toArray(new Menu[menus.size()]));
    }
}
