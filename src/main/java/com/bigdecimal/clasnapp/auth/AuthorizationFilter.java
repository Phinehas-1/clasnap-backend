package com.bigdecimal.clasnapp.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bigdecimal.clasnapp.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final String HEADER_AUTHORIZATION = "Authorization";
  private String username;
  private Collection<SimpleGrantedAuthority> authorities;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    if (!Objects.isNull(request.getHeader(HEADER_AUTHORIZATION))) {
      String authorization = request.getHeader(HEADER_AUTHORIZATION);
      if (requestHasBearerInAuthorizaton(authorization)) authorizeRequestBearer(
        authorization
      );
    }
    filterChain.doFilter(request, response);
  }

  private void authorizeRequestBearer(String authorization) {
    String jwt = authorization.substring(7);
    DecodedJWT decodedJWT = jwtService.decode(jwt);
    username = decodedJWT.getSubject();
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
    authorities =
      Arrays
        .asList(roles)
        .stream()
        .map(role -> {
          return new SimpleGrantedAuthority(role);
        })
        .toList();
    SecurityContextHolder
      .getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(username, null, authorities)
        );
    log.info("Username [ {} ] authorized with roles [ {} ]", username, Arrays.asList(roles));
  }

  private Boolean requestHasBearerInAuthorizaton(String authorization) {
    return authorization.startsWith("Bearer ");
  }
}
