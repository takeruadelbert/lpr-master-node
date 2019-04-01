package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

@Service
public class UserService{

    private UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public Page<User> index(Integer page, Integer size){
        return userRepository.findAll(PageRequest.of(page,size));
    }

    public User create(User o){
        return userRepository.save(o);
    }

    public User get(Long id){
        return userRepository.findById(id).get();
    }

    public User update (Long id,User object){
        User old=userRepository.findById(id).get();
        if (old==null){
            return null;
        }
        object.setId(id);
        return userRepository.save(object);
    }
}
