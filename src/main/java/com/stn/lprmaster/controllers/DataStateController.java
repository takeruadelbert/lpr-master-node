package com.stn.lprmaster.controllers;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.lprmaster.entities.DataState;
import com.stn.lprmaster.entities.enumerate.DataStateStatus;
import com.stn.lprmaster.services.DataStateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/data-state")
public class DataStateController extends CrudController<DataStateService, DataState> {
    public DataStateController(DataStateService dataStateService) {
        super(dataStateService);
    }

    @RequireLogin
    @GetMapping("/status")
    public Map<DataStateStatus, String> getDataStateStatus() {
        return service.getDataStateStatus();
    }
}
