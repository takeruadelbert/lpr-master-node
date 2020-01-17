package com.stn.ester.core.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stn.ester.core.events.LoginEvent;
import com.stn.ester.entities.User;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.services.AuthenticationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.stn.ester.core.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private ApplicationEventPublisher applicationEventPublisher;
    private AuthenticationService authenticationService;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationService authenticationService,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.authenticationManager = authenticationManager;
        this.applicationEventPublisher = applicationEventPublisher;
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        User user = ((User) auth.getPrincipal());
        Collection<GrantedAuthority> authorities = new ArrayList();
        authorities.addAll(auth.getAuthorities());
        //authorities.addAll(accessGroupService.buildAccessAuthorities(user.getUserGroupId()));
        final String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        LocalDateTime loginLocalDateTime = DateTimeHelper.getCurrentLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
        Timestamp now = Timestamp.valueOf(loginLocalDateTime);
        Date loginDate = new Date(now.getTime());
        authenticationService.setLastLogin(user.getId(), loginLocalDateTime);
        String token = JWT.create()
                .withIssuedAt(loginDate)
                .withSubject(user.getUsername())
                .withClaim(AUTHORITIES_KEY, authoritiesString)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader(EXPOSE_HEADER_STRING, HEADER_STRING);
        applicationEventPublisher.publishEvent(new LoginEvent(this, user));
    }
}
