package com.stn.ester.services.crud;

import com.google.common.collect.Sets;
import com.stn.ester.constants.LogoResource;
import com.stn.ester.core.events.RegistrationEvent;
import com.stn.ester.core.exceptions.*;
import com.stn.ester.core.security.SecurityConstants;
import com.stn.ester.dto.entity.UserDTO;
import com.stn.ester.dto.entity.UserSimpleDTO;
import com.stn.ester.entities.*;
import com.stn.ester.entities.enumerate.UserStatus;
import com.stn.ester.helpers.*;
import com.stn.ester.repositories.jpa.*;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.AdvanceSearchTrait;
import com.stn.ester.services.base.traits.AssetFileClaimTrait;
import com.stn.ester.services.base.traits.SimpleSearchTrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends CrudService<User, UserRepository> implements AssetFileClaimTrait, UserDetailsService, SimpleSearchTrait<User, UserSimpleDTO, UserRepository>, AdvanceSearchTrait<User, UserRepository> {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private RoleRepository roleRepository;
    private AssetFileService assetFileService;
    private PasswordResetRepository passwordResetRepository;
    private PasswordResetService passwordResetService;
    private SystemProfileRepository systemProfileRepository;
    private ApplicationEventPublisher eventPublisher;
    private RoleService roleService;
    private RoleGroupRepository roleGroupRepository;
    private RoleGroupService roleGroupService;
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
    public UserService(
            UserRepository userRepository,
            BiodataRepository biodataRepository,
            LoginSessionRepository loginSessionRepository,
            RoleRepository roleRepository,
            AssetFileService assetFileService,
            PasswordResetRepository passwordResetRepository,
            PasswordResetService passwordResetService,
            SystemProfileRepository systemProfileRepository,
            ApplicationEventPublisher eventPublisher,
            RoleService roleService,
            RoleGroupRepository roleGroupRepository,
            RoleGroupService roleGroupService
    ) {
        super(userRepository);
        this.userRepository = userRepository;
        this.loginSessionRepository = loginSessionRepository;
        this.roleRepository = roleRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.assetFileService = assetFileService;
        this.passwordResetService = passwordResetService;
        this.roleService = roleService;
        this.systemProfileRepository = systemProfileRepository;
        this.eventPublisher = eventPublisher;
        this.roleGroupRepository = roleGroupRepository;
        this.roleGroupService = roleGroupService;
    }

    @Override
    @Transactional
    public User create(User user) {
        // set default profile picture
        if (user.getToken() == null) {
            user.setProfilePictureId(AssetFileService.defaultProfilePictureID);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoleIds() != null) {
            for (Long roleId : user.getRoleIds()) {
                Optional<Role> role = roleRepository.findById(roleId);
                user.addRole(role.orElseThrow(() -> new BadRequestException(String.format("Role with id %d not found", roleId))));
            }
        }
        User registeredUser = super.create(user);
        eventPublisher.publishEvent(new RegistrationEvent(this, user));
        return registeredUser;
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        Set<Long> removedRoleIds = null;
        if (user.getRoleIds() != null) {
            Collection<RoleGroup> roleGroup = roleGroupRepository.findAllByUserId(id);
            Set<Long> currentRoleIds = roleGroup.stream().map(role -> role.getRole().getId()).collect(Collectors.toSet());
            removedRoleIds = Sets.difference(currentRoleIds, new HashSet<>(user.getRoleIds()));
            Set<Long> newRoleIds = Sets.difference(new HashSet<>(user.getRoleIds()), currentRoleIds);

            for (Long roleId : newRoleIds) {
                Optional<Role> role = roleRepository.findById(roleId);
                user.addRole(role.orElseThrow(() -> new BadRequestException(String.format("Role with id %d not found", roleId))));
            }
        }
        super.update(id, user);
        for (Long roleId : removedRoleIds) {
            Optional<RoleGroup> toDeleteRoleGroup = roleGroupRepository.findByRoleIdAndUserId(roleId, id);
            if (toDeleteRoleGroup.isPresent()) {
                toDeleteRoleGroup.get().getUser().getRoleGroups().clear();
                roleGroupService.delete(toDeleteRoleGroup.get().getId());
            }
        }
        User saved = get(id);
        entityManager.flush();
        entityManager.refresh(saved);
        return saved;
    }

    @Transactional
    public Object changePassword(Long userID, String oldPassword, String newPassword, String retypeNewPassword) {
        System.out.println(userID);
        // check if old password is same with the registered one
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException();
        }
        return changePassword(userID, newPassword, retypeNewPassword);
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
        return super.update(userID, user);
    }

    @Override
    public String getAssetPath() {
        return this.asset_path;
    }

    @Override
    public AssetFile claimFile(String token) {
        return this.assetFileService.moveTempDirToPermanentDir(token, this.getAssetPath());
    }

    @Transactional
    public Object changeProfilePicture(String token) {
        if (!token.isEmpty()) {
            Long user_id = SessionHelper.getUserID();
            AssetFile assetFile = this.claimFile(token);
            User user = this.userRepository.findById(user_id).get();
            user.setId(user_id);
            user.setProfilePictureId(assetFile.getId());
            user.setProfilePicture(assetFile);
            return super.update(user_id, user);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.put("message", "Failed to change profile picture : Invalid Token.");
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
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
    public Object createIfUsernameNotExist(User user, String username) {
        if (!this.userRepository.findByUsername(username).isPresent()) {
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
                    passwordResetService.update(passwordReset.getId(), passwordReset);
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
                MimeMessageHelper helper = new MimeMessageHelper(message, true, LogoResource.ENCODING_UTF_8);
                helper.setFrom(EmailHelper.emailFrom());
                helper.setTo(EmailHelper.emailTo(user.get().getEmail()));
                helper.setSubject(EmailHelper.emailSubject());

                Context context = new Context();
                String linkResetPassword = EmailHelper.createLinkResetPassword(token, request);
                SystemProfile systemProfile = this.systemProfileRepository.findById(1L).get();
                context.setVariable(LogoResource.CONTEXT_VARIABLE_USERNAME, user.get().getUsername());
                context.setVariable(LogoResource.CONTEXT_VARIABLE_ACTION, linkResetPassword);
                context.setVariable(LogoResource.CONTEXT_VARIABLE_ADDRESS, systemProfile.getAddress());
                context.setVariable(LogoResource.CONTEXT_VARIABLE_NAME, systemProfile.getName());
                context.setVariable(LogoResource.CONTEXT_VARIABLE_WEBSITE, systemProfile.getWebsite());
                String htmlFile = templateEngine.process(LogoResource.TEMPLATE_MAIL_FILE, context);
                EmailHelper.embeddedImageOnTemplateMail(htmlFile, message);
                mailSender.send(message);

                PasswordReset passwordReset = this.passwordResetRepository.findByUserId(user.get().getId());
                // Update is used to default if user make re-request password reset.
                if (passwordReset.getIsUsed() == 1) {
                    passwordReset.setId(passwordReset.getId());
                    passwordReset.setIsUsed(0);
                    passwordResetService.update(passwordReset.getId(), passwordReset);
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
            passwordResetService.update(passwordReset.getId(), passwordReset);
            // Updates new password user.
            user.setId(passwordReset.getUserId());
            user.setPassword(this.passwordEncoder.encode(new_password));
            super.update(passwordReset.getId(), user);
        }
        return user;
    }

    public Map<UserStatus, String> getUserStatusList() {
        Map<UserStatus, String> result = new HashMap<>();
        List<UserStatus> userStatuses = UserStatus.toList();
        for (UserStatus userStatus : userStatuses) {
            result.put(userStatus, userStatus.getLabel());
        }
        return result;
    }

    public void changeUserStatus(Long userId, UserStatus userStatus) {
        Optional<User> user = currentEntityRepository.findById(userId);
        if (!user.isPresent()) {
            throw new InvalidValueException("User Not Found");
        }
        user.get().setUserStatus(userStatus);
        currentEntityRepository.save(user.get());
    }

    public Collection<UserDTO> searchSuperAdmin(String keyword) {
        //superadmin specification
        Long superAdminId = this.roleService.getIdByName(SecurityConstants.ROLE_SUPERADMIN);
        Specification superAdminSpecification = (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            final Subquery<Long> roleGroupQuery = criteriaQuery.subquery(Long.class);
            final Root<RoleGroup> roleGroupRoot = roleGroupQuery.from(RoleGroup.class);
            final Join<User, RoleGroup> userRoleGroupJoin = root.join("roleGroups");
            roleGroupQuery.select(roleGroupRoot.<Long>get("user"));
            roleGroupQuery.where(criteriaBuilder.equal(roleGroupRoot.<Long>get("role"), superAdminId));
            final Path<User> path = userRoleGroupJoin.<User>get("id");
            return path.in(roleGroupQuery);
        };
        //search keyword
        Specification keywordSpecification = SearchAndFilterHelper.resolveSpecificationSingleKeyword(getSimpleSearchKeys(), keyword);
        return advanceSearch(Specification.where(keywordSpecification).and(superAdminSpecification), UserDTO.class);
    }

    @Override
    public Collection<String> getSimpleSearchKeys() {
        List<String> keys = new ArrayList<>();
        keys.add("username");
        keys.add("email");
        keys.add("biodata.firstName");
        keys.add("biodata.lastName");
        return keys;
    }

    @Override
    public UserRepository getRepository() {
        return currentEntityRepository;
    }
}
