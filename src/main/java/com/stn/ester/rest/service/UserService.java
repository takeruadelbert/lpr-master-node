package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

@Service
public class UserService extends AppService<UserRepository>{

    @Autowired
    public UserService(UserRepository userRepository){
        super(userRepository);
    }

}
