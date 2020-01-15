package com.stn.ester.controllers.function;

import com.stn.ester.controllers.base.SecuredController;
import com.stn.ester.core.base.AccessAllowed;
import com.stn.ester.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController extends SecuredController {

    private GeneralService generalService;

    @Autowired
    public GeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }


    @AccessAllowed
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getServerTime() {
        return generalService.getStatus();
    }

}
