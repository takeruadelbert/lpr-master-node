package com.stn.ester.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stn.ester.core.exceptions.MultipleLoginException;
import com.stn.ester.entities.User;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.services.AuthenticationService;
import com.stn.ester.services.crud.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.stn.ester.core.security.SecurityConstants.*;

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
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            User user = (User) userService.loadUserByUsername(authentication.getPrincipal().toString());
            if (!user.isAccountNonLocked()) {
                throw new LockedException("Account banned.");
            } else if (!user.isAccountNonExpired()) {
                throw new LockedException("Account disbled.");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        } catch (MultipleLoginException mle) {
            res.setStatus(440);
        } catch (TokenExpiredException ex) {
            res.sendError(401, "Token expired.");
        } catch (DisabledException e) {
            res.sendError(401, "Account disbled.");
        } catch (LockedException e) {
            res.sendError(401, "Account banned.");
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String username = decodedJWT.getSubject();
            String authoritiesString = decodedJWT.getClaim(AUTHORITIES_KEY).asString();
            Date iat = decodedJWT.getIssuedAt();
            final Collection authorities =
                    Arrays.stream(authoritiesString.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            if (username != null) {
                if (!authenticationService.isMultipleSessionAllowed() && !authenticationService.isNewestToken(username, DateTimeHelper.asLocalDateTime(iat))) {
                    throw new MultipleLoginException();
                }
                System.out.println("granted auth : " + authoritiesString);
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
            return null;
        }
        return null;
    }
}
