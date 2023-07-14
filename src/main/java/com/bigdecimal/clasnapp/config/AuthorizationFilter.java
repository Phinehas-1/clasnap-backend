package com.bigdecimal.clasnapp.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bigdecimal.clasnapp.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final String HEADER_AUTHORIZATION = "Authorization";
    private String username;
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (hasHeader(request, HEADER_AUTHORIZATION) && authHeaderIsBearer(request)) {
            log.info("Decoding authorization header Bearer token.");
            String jwt = request.getHeader(HEADER_AUTHORIZATION).substring(7);
            DecodedJWT decodedJWT = jwtService.decode(jwt);
            username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            log.info("{} has ", username);
            authorities = Arrays.asList(roles).stream()
                    .map(role -> {
                        log.info("{} role.", role);
                        return new SimpleGrantedAuthority(role);
                    }).toList();
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }
        filterChain.doFilter(request, response);
    }

    private Boolean hasHeader(HttpServletRequest request, String header) {
        return !Objects.isNull(request.getHeader(header));
    }

    private Boolean authHeaderIsBearer(HttpServletRequest request) {
        return request.getHeader(HEADER_AUTHORIZATION).startsWith("Bearer ");
    }
}
