package com.stn.ester.controllers.report;

import com.stn.ester.controllers.base.SecuredController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportTesterController extends SecuredController {

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('testPOST'))")
    @RequestMapping(value = "/report/test", method = RequestMethod.POST)
    public void testAccessGroup() {

    }
}
