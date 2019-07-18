package com.stn.ester.rest.service;

import com.stn.ester.rest.RestApplication;
import com.stn.ester.rest.dao.jpa.*;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.exception.*;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.EmailHelper;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService extends AppService implements AssetFileBehaviour {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private UserGroupRepository userGroupRepository;
    private AssetFileService assetFileService;
    private PasswordResetRepository passwordResetRepository;
    private String asset_path = "profile_picture";

    @Value("${ester.session.login.timeout}")
    private int sessionTimeout;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    public UserService(UserRepository userRepository, BiodataRepository biodataRepository, LoginSessionRepository loginSessionRepository, UserGroupRepository userGroupRepository, AssetFileService assetFileService, PasswordResetRepository passwordResetRepository) {
        super(User.unique_name);
        super.repositories.put(User.unique_name, userRepository);
        super.repositories.put(Biodata.unique_name, biodataRepository);
        super.repositories.put(UserGroup.unique_name, userGroupRepository);
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.userGroupRepository = userGroupRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.assetFileService = assetFileService;
    }

    @Override
    @Transactional
    public Object create(AppDomain o) {
        // set default profile picture
        if (((User) o).getToken() == null) {
            ((User) o).setAssetFileId(RestApplication.defaultProfilePictureID);
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
        if (!token.isEmpty()) {
            Long user_id = SessionHelper.getUserID();
            Long asset_file_id = this.claimFile(token);
            User user = this.userRepository.findById(user_id).get();
            user.setId(user_id);
            user.setAssetFileId(asset_file_id);
            return super.update(user_id, user);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.put("message", "Failed to change profile picture : Invalid Token.");
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public Object identifyEmail(String email) {
        Map<String, Object> result = new HashMap<>();
        if (!email.isEmpty()) {
            try {
                Optional<User> user = this.userRepository.findByEmail(email);
                if (!user.equals(Optional.empty())) {
                    String token = GlobalFunctionHelper.generateToken();

                    // If user have token to reset password then update data if not have token next to add new data.
                    PasswordReset updatePasswordReset = this.passwordResetRepository.findByUserId(user.get().getId());
                    if (updatePasswordReset != null) {
                        updatePasswordReset.setToken(token);
                        updatePasswordReset.setExpire(DateTimeHelper.getDateTimeNowPlusSeveralDays(1));
                        updatePasswordReset.setUserId(user.get().getId());
                        // Update data into db.
                        this.passwordResetRepository.save(updatePasswordReset);
                    } else {
                        PasswordReset addNewPasswordReset = new PasswordReset();
                        addNewPasswordReset.setToken(token);
                        addNewPasswordReset.setExpire(DateTimeHelper.getDateTimeNowPlusSeveralDays(1));
                        addNewPasswordReset.setUserId(user.get().getId());
                        // Add new data into db.
                        this.passwordResetRepository.save(addNewPasswordReset);
                    }

                    // If e-mail found send link reset password to user.
                    sendLinkResetPassword(user, token);

                    result.put("status", HttpStatus.OK.value());
                    result.put("message", "Link reset password has been sent to your e-mail.");
                } else {
                    result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.put("message", "Email not found.");
                }
            } catch (Exception ex) {
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                result.put("message", ex.getMessage());
            }
        }
        return result;
    }

    public Object sendLinkResetPassword(Optional<User> user, String token) {
        Map<String, Object> result = new HashMap<>();
        if (!user.equals(null)) {

            // Setting e-mail properties.
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.auth", "true");

            // Getting hostname, port and others from http servlet request
            String Scheme = String.valueOf(httpServletRequest.getScheme());
            String ServerName = httpServletRequest.getServerName();
            String RequestURI = httpServletRequest.getRequestURI();
            String ServerPort = String.valueOf(httpServletRequest.getServerPort());

            // Check smtp username and password from server.
            Session session = EmailHelper.passwordAuthentication(prop);
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EmailHelper.emailFrom()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailHelper.emailTo()));
                message.setSubject(EmailHelper.emailSubject());
                message.setText(EmailHelper.emailTemplate(user, Scheme, ServerName, ServerPort, token));
                Transport.send(message);
            } catch (Exception ex) {
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                result.put("message", ex.getMessage());
            }
        }
        return result;
    }

    public Object resetPassword(String token) {
        Map<String, Object> result = new HashMap<>();
        if (!token.isEmpty()) {
            try {
                PasswordReset passwordReset = this.passwordResetRepository.findByToken(token);
                Date getExpire = passwordReset.getExpire();
                if (DateTimeHelper.getDateTimeNow().before(getExpire)) {
                    EmailHelper.resetPasswordToken += token;

                    result.put("status", HttpStatus.OK.value());
                    result.put("message", "Token found.");
                } else {
                    result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.put("message", "Token not found or token is expired.");
                }
            } catch (Exception ex) {
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                result.put("message", ex.getMessage());
            }
        }
        return result;
    }

    public Object confirmResetPassword(String new_password, String confirm_password) {
        PasswordReset passwordReset = this.passwordResetRepository.findByToken(EmailHelper.resetPasswordToken);
        User user = this.userRepository.findById(passwordReset.getUserId()).orElse(null);

        if (new_password.isEmpty()) {throw new EmptyFieldException("New password is empty!");}
        if (confirm_password.isEmpty()) {throw new EmptyFieldException("Confirm password is empty!");}
        if (!new_password.isEmpty() && !confirm_password.isEmpty()) {
            if (passwordReset.equals(null) && user.equals(null)) {
                throw new UnauthorizedException("User not found.");
            }
            if (!new_password.equals(confirm_password)) {
                throw new ConfirmNewPasswordException("New Password and Confirm Password is different.");
            }

            passwordReset.setId(passwordReset.getId());
            passwordReset.setIsUsed(passwordReset.getIsUsed());
            // Update data into db table passwordReset.
            this.passwordResetRepository.save(passwordReset);

            user.setId(passwordReset.getUserId());
            user.setPassword(this.passwordEncoder.encode(new_password));
            // Update data into db table user.
            this.userRepository.save(user);
        }
        throw new OkException("Your new password is succesfully created.");
    }
}
