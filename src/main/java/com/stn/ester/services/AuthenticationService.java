package com.stn.ester.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stn.ester.core.exceptions.MultipleLoginException;
import com.stn.ester.entities.User;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.repositories.jpa.UserRepository;
import com.stn.ester.services.crud.SystemProfileService;
import com.stn.ester.services.crud.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.stn.ester.constants.AuthMessage.ACCOUNT_BANNED_STRING;
import static com.stn.ester.constants.AuthMessage.ACCOUNT_DISABLED_STRING;
import static com.stn.ester.core.security.SecurityConstants.*;

@Service
@Log4j2
public class AuthenticationService {

    private UserRepository userRepository;
    private SystemProfileService systemProfileService;
    private UserService userService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 SystemProfileService systemProfileService,
                                 UserService userService) {
        this.userRepository = userRepository;
        this.systemProfileService = systemProfileService;
        this.userService = userService;
    }

    public void setLastLogin(Long userId, LocalDateTime loginTime) {
        User user = this.userRepository.findById(userId).get();
        user.setLastLogin(loginTime);
        this.userRepository.save(user);
    }

    public boolean isNewestToken(String username, LocalDateTime iat) {
        User user = this.userRepository.findByUsername(username).get();
        return user.getLastLogin().compareTo(iat) == 0;
    }

    public Boolean isMultipleSessionAllowed() {
        return this.systemProfileService.get().getMultipleSession();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String jwtToken) {
        if (jwtToken != null) {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(jwtToken.replace(AUTHORIZATION_TOKEN_PREFIX, ""));
            String username = decodedJWT.getSubject();
            String authoritiesString = decodedJWT.getClaim(AUTHORITIES_KEY).asString();
            Date iat = decodedJWT.getIssuedAt();
            final Collection authorities =
                    Arrays.stream(authoritiesString.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            if (username != null) {
                if (!isMultipleSessionAllowed() && !isNewestToken(username, DateTimeHelper.asLocalDateTime(iat))) {
                    throw new MultipleLoginException();
                }
                User user = (User) userService.loadUserByUsername(username);
                if (!user.isAccountNonLocked()) {
                    throw new LockedException(ACCOUNT_BANNED_STRING);
                } else if (!user.isAccountNonExpired()) {
                    throw new LockedException(ACCOUNT_DISABLED_STRING);
                }
                Map<String, Object> credentials = new HashMap<>();
                credentials.put("jwtToken", jwtToken);
                credentials.put("userId", userService.getUserIdByUsername(username));
                return new UsernamePasswordAuthenticationToken(username, credentials, authorities);
            }
            return null;
        }
        return null;
    }
}
