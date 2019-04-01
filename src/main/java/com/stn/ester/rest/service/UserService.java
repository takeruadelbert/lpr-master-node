package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AppService{

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository){
        super("user");
        super.repositories.put("user",userRepository);
        super.repositories.put("biodata",biodataRepository);
    }

}
