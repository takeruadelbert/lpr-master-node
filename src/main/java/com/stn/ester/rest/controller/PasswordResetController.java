package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.PasswordReset;
import com.stn.ester.rest.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password_reset")
public class PasswordResetController extends AppController<PasswordResetService, PasswordReset> {

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {super(passwordResetService);}
}
