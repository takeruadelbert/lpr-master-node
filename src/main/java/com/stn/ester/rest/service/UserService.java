package com.stn.ester.rest.service;

import com.stn.ester.rest.RestApplication;
import com.stn.ester.rest.dao.jpa.BiodataRepository;
import com.stn.ester.rest.dao.jpa.LoginSessionRepository;
import com.stn.ester.rest.dao.jpa.UserGroupRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.dao.jpa.*;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.exception.*;
import com.stn.ester.rest.helper.*;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.*;

@Service
public class UserService extends AppService implements AssetFileBehaviour, UserDetailsService {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private UserGroupRepository userGroupRepository;
    private AssetFileService assetFileService;
    private PasswordResetRepository passwordResetRepository;
    private PasswordResetService passwordResetService;
    private SystemProfileRepository systemProfileRepository;
    private String asset_path = "profile_picture";

    @Value("${ester.session.login.timeout}")
    private int sessionTimeout;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository, LoginSessionRepository loginSessionRepository, UserGroupRepository userGroupRepository, AssetFileService assetFileService, PasswordResetRepository passwordResetRepository, PasswordResetService passwordResetService, SystemProfileRepository systemProfileRepository) {
        super(User.unique_name);
        super.repositories.put(User.unique_name, userRepository);
        super.repositories.put(Biodata.unique_name, biodataRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.userGroupRepository = userGroupRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.assetFileService = assetFileService;
        this.passwordResetService = passwordResetService;
        this.systemProfileRepository = systemProfileRepository;
    }

    @Override
    @Transactional
    public Object create(AppDomain o) {
        // set default profile picture
        if (((User) o).getToken() == null) {
            ((User) o).setAssetFileId(AssetFileService.defaultProfilePictureID);
        }
        ((User) o).setPassword(passwordEncoder.encode(((User) o).getPassword()));
        return super.create(o);
    }

    @Override
    public Object update(Long id, AppDomain object) {
        return super.update(id, object);
    }

    public Map login(String username, String password, HttpSession session) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
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

    @Transactional
    public Object changePassword(Long userID, String oldPassword, String newPassword, String retypeNewPassword) {
        System.out.println(userID);
        // check if old password is same with the registered one
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException();
        }
        return changePassword(userID, newPassword,retypeNewPassword);
    }

    @Transactional
    public Object changePassword(Long userID, String newPassword, String retypeNewPassword) {
        // check if new password is same with retype one
        if (!newPassword.equals(retypeNewPassword)) {
            throw new ConfirmNewPasswordException();
        }

        User user = this.userRepository.findById(userID).orElse(null);

        user.setId(userID);
        user.setPassword(this.passwordEncoder.encode(newPassword));
        return super.save(user);
    }

    @Override
    public String getAssetPath() {
        return this.asset_path;
    }

    @Override
    public AssetFile claimFile(String token) {
        return this.assetFileService.moveTempDirToPermanentDir(token, this.getAssetPath());
    }

    public Object changeProfilePicture(String token) {
        if (!token.isEmpty()) {
            Long user_id = SessionHelper.getUserID();
            AssetFile assetFile = this.claimFile(token);
            User user = this.userRepository.findById(user_id).get();
            user.setId(user_id);
            user.setAssetFileId(assetFile.getId());
            user.setAssetFile(assetFile);
            return super.update(user_id, user);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.put("message", "Failed to change profile picture : Invalid Token.");
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public void addDefaultProfilePicture() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usersOptional = userRepository.findByUsername(username);

        usersOptional
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return usersOptional
                .get();
    }

    @Transactional
    public Object createIfUsernameNotExist(User user,String username){
        if (!this.userRepository.findByUsername(username).isPresent()){
            return this.create(user);
        }
        return null;
    }

    public Object identifyEmail(String email, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> user = this.userRepository.findByEmail(email);
            if (!user.equals(Optional.empty())) {
                String token = GlobalFunctionHelper.generateToken();
                // Check while user have token start to update data password reset if not have create new password reset.
                PasswordReset passwordReset = this.passwordResetRepository.findByUserId(user.get().getId());
                if (passwordReset != null) {
                    passwordReset.setId(passwordReset.getId());
                    passwordReset.setToken(token);
                    passwordReset.setExpire(DateTimeHelper.getDateTimeNowPlusSeveralDays(1));
                    super.update(passwordReset.getId(), passwordReset);
                } else {
                    PasswordReset pReset = new PasswordReset();
                    pReset.setToken(token);
                    pReset.setExpire(DateTimeHelper.getDateTimeNowPlusSeveralDays(1));
                    pReset.setUserId(user.get().getId());
                    passwordResetService.create(pReset);
                }
                sendingEmail(user, token, request);

                result.put("status", HttpStatus.OK.value());
                result.put("message", "Kami telah mengirimi anda email. \n Silakan cek e-mail anda, untuk mereset password silakan klik link yang terdapat pada email anda.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                result.put("message", "Email tidak ditemukan.");
                return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception ex) {
            result.put("status", HttpStatus.BAD_REQUEST.value());
            result.put("message", ex.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    public Object sendingEmail(Optional<User> user, String token, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, ConstantHelper.ENCODING_UTF_8);
                helper.setFrom(EmailHelper.emailFrom());
                helper.setTo(EmailHelper.emailTo(user.get().getEmail()));
                helper.setSubject(EmailHelper.emailSubject());

                Context context = new Context();
                String linkResetPassword = EmailHelper.createLinkResetPassword(token, request);
                SystemProfile systemProfile = this.systemProfileRepository.findById(1L).get();
                context.setVariable(ConstantHelper.CONTEXT_VARIABLE_USERNAME, user.get().getUsername());
                context.setVariable(ConstantHelper.CONTEXT_VARIABLE_ACTION, linkResetPassword);
                context.setVariable(ConstantHelper.CONTEXT_VARIABLE_ADDRESS, systemProfile.getAddress());
                context.setVariable(ConstantHelper.CONTEXT_VARIABLE_NAME, systemProfile.getName());
                context.setVariable(ConstantHelper.CONTEXT_VARIABLE_WEBSITE, systemProfile.getWebsite());
                String htmlFile = templateEngine.process(ConstantHelper.TEMPLATE_MAIL_FILE, context);
                EmailHelper.embeddedImageOnTemplateMail(htmlFile, message);
                mailSender.send(message);

                PasswordReset passwordReset = this.passwordResetRepository.findByUserId(user.get().getId());
                // Update is used to default if user make re-request password reset.
                if (passwordReset.getIsUsed() == 1) {
                    passwordReset.setId(passwordReset.getId());
                    passwordReset.setIsUsed(0);
                    super.update(passwordReset.getId(), passwordReset);
                }
            } catch (Exception ex) {
                result.put("status", HttpStatus.BAD_REQUEST.value());
                result.put("message", ex.getMessage());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }
        return result;
    }

    public Object passwordReset(String token, String new_password, String confirm_password) {
        Map<String, Object> result = new HashMap<>();
        if (!token.isEmpty()) {
            try {
                PasswordReset passwordReset = this.passwordResetRepository.findByToken(token);
                if (passwordReset == null) {
                    throw new NotFoundException("Token tidak ditemukan.");
                }
                Date getExpire = passwordReset.getExpire();
                if (DateTimeHelper.getDateTimeNow().before(getExpire)) {
                    confirmPasswordReset(passwordReset, new_password, confirm_password);

                    result.put("status", HttpStatus.OK.value());
                    result.put("message", "Password baru telah berhasil dibuat.");
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.put("message", "Token tidak ditemukan atau token telah kadaluwarsa.");
                    return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } catch (Exception ex) {
                result.put("status", HttpStatus.BAD_REQUEST.value());
                result.put("message", ex.getMessage());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }
        return result;
    }

    public Object confirmPasswordReset(PasswordReset pReset, String new_password, String confirm_password) {
        PasswordReset passwordReset = this.passwordResetRepository.findByToken(pReset.getToken());
        User user = this.userRepository.findById(passwordReset.getUserId()).orElse(null);
        if (new_password.isEmpty())
            throw new EmptyFieldException("Silakan isi bidang password baru!");
        if (confirm_password.isEmpty())
            throw new EmptyFieldException("Silakan isi bidang konfirmasi password!");
        if (!new_password.isEmpty() && !confirm_password.isEmpty()) {
            if (passwordReset.equals(null) && user.equals(null))
                throw new UnauthorizedException("User tidak ditemukan.");
            if (!new_password.equals(confirm_password))
                throw new ConfirmNewPasswordException("Password baru dan konfirmasi password tidak sesuai.");
            // Set is used not to be default if user has been create new password.
            passwordReset.setId(passwordReset.getId());
            passwordReset.setIsUsed(1);
            super.update(passwordReset.getId(), passwordReset);
            // Updates new password user.
            user.setId(passwordReset.getUserId());
            user.setPassword(this.passwordEncoder.encode(new_password));
            super.update(passwordReset.getId(), user);
        }
        return user;
    }
}
