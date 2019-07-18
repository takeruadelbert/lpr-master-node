package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.LoginSessionRepository;
import com.stn.ester.rest.domain.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionService extends AppService {

    private LoginSessionRepository loginSessionRepository;

    @Autowired
    public LoginSessionService(LoginSessionRepository loginSessionRepository) {
        super(LoginSession.unique_name);
        super.repositories.put(LoginSession.unique_name, loginSessionRepository);
        this.loginSessionRepository=loginSessionRepository;
    }



}
