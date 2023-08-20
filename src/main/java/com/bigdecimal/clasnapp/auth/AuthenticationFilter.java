package com.bigdecimal.clasnapp.auth;

import com.bigdecimal.clasnapp.domain.LoginDto;
import com.bigdecimal.clasnapp.exception.InvalidUserDetailsException;
import com.bigdecimal.clasnapp.util.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private LoginDto loginDto;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    String userParams[] = getUserParamsFromRequestBody(request);
    return authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(userParams[0], userParams[1])
    );
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    User user = (User) authResult.getPrincipal();
    String accessToken = jwtService.accessToken(user);
    response.setContentType("application/json;charset=utf-8");
    try (PrintWriter out = response.getWriter()) {
      out.print(String.format("{\"accessToken\":\"%s\"}", accessToken));
    } catch (Exception e) {
      response.setHeader("access_token", accessToken);
    }
    log.info("User [ {} ] authenticated successfully.", user.getUsername());
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  ) throws IOException, ServletException {
    response.setStatus(401);
    log.info("Authentication failed for username [ {} ].", loginDto.username());
  }

  private String[] getUserParamsFromRequestBody(HttpServletRequest request) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      loginDto = mapper.readValue(request.getReader(), LoginDto.class);
    } catch (Exception e) {
      throw new InvalidUserDetailsException("Read user login details failed.");
    }
    return new String[] { loginDto.username(), loginDto.password() };
  }
}
