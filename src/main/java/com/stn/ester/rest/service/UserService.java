package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.LoginSessionRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.exception.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService extends AppService{

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private UserGroupRepository userGroupRepository;

    @Value("${ester.session.login.timeout}")
    private int sessionTimeout;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository, LoginSessionRepository loginSessionRepository, UserGroupRepository userGroupRepository){
        super(User.unique_name);
        super.repositories.put(User.unique_name,userRepository);
        super.repositories.put(Biodata.unique_name,biodataRepository);
        super.repositories.put(UserGroup.unique_name,userGroupRepository);
        this.userRepository=userRepository;
        this.loginSessionRepository=loginSessionRepository;
        this.userGroupRepository=userGroupRepository;
    }

    @Override
    @Transactional
    public Object create(AppDomain o) {
        ((User) o).setPassword(passwordEncoder.encode(((User) o).getPassword()));
        return super.create(o);
    }

    public Map login(String username, String password, HttpSession session){
        User user=userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password,user.getPassword())){
            throw new InvalidLoginException();
        }
        UUID randomUUID = UUID.randomUUID();
        String token=randomUUID.toString();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,sessionTimeout);
        LoginSession loginSession=new LoginSession(token,calendar.getTime(),user);
        LoginSession savedLoginSession=loginSessionRepository.save(loginSession);
        Map o=new HashMap();
        o.put("username",username);
        o.put("token",token);

        Map<String,Object> loginInfoSession=new HashMap();

        loginInfoSession.put("token",token);
        loginInfoSession.put("username",username);
        loginInfoSession.put("loginSessionId",savedLoginSession.getId());

        session.setAttribute("login",loginInfoSession);
        return o;
    }

    public LoginSession isValidToken(String token){
        return loginSessionRepository.isTokenExist(token);
    }

    public LoginSession tokenHeartbeat(String token){
        LoginSession loginSession = loginSessionRepository.isTokenExist(token);
        if (loginSession==null){
            return null;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,sessionTimeout);
        loginSession.setExpire(calendar.getTime());
        LoginSession savedLoginSession=loginSessionRepository.save(loginSession);
        return savedLoginSession;
    }
}
