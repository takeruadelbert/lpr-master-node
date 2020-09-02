package com.stn.ester.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.stn.ester.core.security.SecurityConstants.*;

public class AuthUtil {

    public static UsernamePasswordAuthenticationToken generateAuthToken(String jwtToken){
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

            System.out.println("granted auth : " + authoritiesString);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
