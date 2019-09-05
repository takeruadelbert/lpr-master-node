package com.stn.ester.rest.controller.function;

import com.stn.ester.rest.base.AccessAllowed;
import com.stn.ester.rest.controller.base.SecuredController;
import com.stn.ester.rest.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController extends SecuredController {

    private GeneralService generalService;

    @Autowired
    public GeneralController(GeneralService generalService){
        this.generalService=generalService;
    }


    @AccessAllowed
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getServerTime() {
        return generalService.getStatus();
    }
}
