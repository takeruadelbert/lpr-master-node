package com.stn.ester.services.crud;

import com.stn.ester.repositories.jpa.LoginSessionRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionService extends CrudService {

    private LoginSessionRepository loginSessionRepository;

    @Autowired
    public LoginSessionService(LoginSessionRepository loginSessionRepository) {
        super(loginSessionRepository);
        this.loginSessionRepository = loginSessionRepository;
    }


}
