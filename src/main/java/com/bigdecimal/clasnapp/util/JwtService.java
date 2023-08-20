package com.bigdecimal.clasnapp.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
    private static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    private JWTVerifier verifier;

    public String accessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .sign(algorithm);
    }

    public DecodedJWT decode(String jwt) {
        try {
            verifier = JWT.require(algorithm).build();
        } catch (JWTVerificationException jex) {
            log.error("JWT verification failed because {}", jex.getCause());
        } catch (Exception e) {
            log.error("Authorization failed because {}", e.getCause());
        }
        return verifier.verify(jwt);
    }
}
