package com.stn.ester.core.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.stn.ester.core.exceptions.MultipleLoginException;
import com.stn.ester.services.AuthenticationService;
import com.stn.ester.services.crud.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.stn.ester.constants.AuthMessage.*;
import static com.stn.ester.core.security.SecurityConstants.AUTHORIZATION_HEADER_STRING;
import static com.stn.ester.core.security.SecurityConstants.AUTHORIZATION_TOKEN_PREFIX;

@Log4j2
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationService authenticationService;
    private UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authManager,
                                  AuthenticationService authenticationService,
                                  UserService userService) {
        super(authManager);
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AUTHORIZATION_HEADER_STRING);

        if (header == null || !header.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = authenticationService.getAuthentication(req.getHeader(AUTHORIZATION_HEADER_STRING));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        } catch (MultipleLoginException mle) {
            res.setStatus(440);
        } catch (TokenExpiredException ex) {
            res.sendError(401, TOKEN_EXPIRED_STRING);
        } catch (DisabledException e) {
            res.sendError(401, ACCOUNT_DISABLED_STRING);
        } catch (LockedException e) {
            res.sendError(401, ACCOUNT_BANNED_STRING);
        }
    }

}
