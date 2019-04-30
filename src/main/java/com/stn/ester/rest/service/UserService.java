package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.LoginSessionRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Biodata;
import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.exception.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService extends AppService{

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;

    @Value("${ester.session.login.timeout}")
    private int sessionTimeout;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository,LoginSessionRepository loginSessionRepository){
        super(User.unique_name);
        super.repositories.put(User.unique_name,userRepository);
        super.repositories.put(Biodata.unique_name,biodataRepository);
        this.userRepository=userRepository;
        this.loginSessionRepository=loginSessionRepository;
    }

    @Override
    public Object create(AppDomain o) {
        ((User) o).setPassword(passwordEncoder.encode(((User) o).getPassword()));
        return super.create(o);
    }

    public Map login(String username, String password){
        User user=userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password,user.getPassword())){
            throw new InvalidLoginException("Username or password invalid.");
        }
        UUID randomUUID = UUID.randomUUID();
        String token=randomUUID.toString();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,sessionTimeout);
        LoginSession loginSession=new LoginSession(token,calendar.getTime(),user);
        loginSessionRepository.save(loginSession);
        Map o=new HashMap();
        o.put("username",username);
        o.put("token",token);
        return o;
    }

}
