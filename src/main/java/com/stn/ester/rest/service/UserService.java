package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.LoginSessionRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.exception.ConfirmNewPasswordException;
import com.stn.ester.rest.exception.InvalidLoginException;
import com.stn.ester.rest.exception.PasswordMismatchException;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService extends AppService implements AssetFileBehaviour {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private UserGroupRepository userGroupRepository;
    private AssetFileService assetFileService;
    private String asset_path = "profile_picture";

    @Value("${ester.session.login.timeout}")
    private int sessionTimeout;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository, LoginSessionRepository loginSessionRepository, UserGroupRepository userGroupRepository, AssetFileService assetFileService) {
        super(User.unique_name);
        super.repositories.put(User.unique_name, userRepository);
        super.repositories.put(Biodata.unique_name, biodataRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.userGroupRepository = userGroupRepository;
        this.assetFileService = assetFileService;
    }

    @Override
    @Transactional
    public Object create(AppDomain o) {
        ((User) o).setPassword(passwordEncoder.encode(((User) o).getPassword()));
        return super.create(o);
    }

    @Override
    public Object update(Long id, AppDomain object){
        return super.update(id,object);
    }

    public Map login(String username, String password, HttpSession session){
        User user=userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password,user.getPassword())){
            throw new InvalidLoginException();
        }
        UUID randomUUID = UUID.randomUUID();
        String token = randomUUID.toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, sessionTimeout);
        LoginSession loginSession = new LoginSession(token, calendar.getTime(), user);
        LoginSession savedLoginSession = loginSessionRepository.save(loginSession);
        Map o = new HashMap();
        o.put("username", username);
        o.put("token", token);

        Map<String, Object> loginInfoSession = new HashMap();

        loginInfoSession.put("token", token);
        loginInfoSession.put("username", username);
        loginInfoSession.put("loginSessionId", savedLoginSession.getId());

        session.setAttribute("login", loginInfoSession);
        return o;
    }

    public LoginSession isValidToken(String token) {
        return loginSessionRepository.isTokenExist(token);
    }

    public LoginSession tokenHeartbeat(String token) {
        LoginSession loginSession = loginSessionRepository.isTokenExist(token);
        if (loginSession == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, sessionTimeout);
        loginSession.setExpire(calendar.getTime());
        LoginSession savedLoginSession = loginSessionRepository.save(loginSession);
        return savedLoginSession;
    }

    public Object changePassword(Long userID, String oldPassword, String newPassword, String retypeNewPassword) {
        // check if new password is same with retype one
        if (!newPassword.equals(retypeNewPassword)) {
            throw new ConfirmNewPasswordException();
        }

        // check if old password is same with the registered one
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException();
        }

        user.setId(userID);
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
        return user;
    }

    @Override
    public String getAssetPath() {
        return this.asset_path;
    }

    @Override
    public Long claimFile(String token) {
        return this.assetFileService.moveTempDirToPermanentDir(token, this.getAssetPath());
    }

    public Object changeProfilePicture(String token) {
        if(!token.isEmpty()) {
            Long user_id = SessionHelper.getUserID();
            Long asset_file_id = this.claimFile(token);
            User user = this.userRepository.findById(user_id).get();
            user.setId(user_id);
            user.setAssetFileId(asset_file_id);
            return super.update(user_id, user);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.put("message", "Failed to change profile picture : Invalid Token.");
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
