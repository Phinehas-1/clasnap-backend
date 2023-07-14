package com.bigdecimal.clasnapp.config;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bigdecimal.clasnapp.JwtService;
import com.bigdecimal.clasnapp.config.exceptions.InvalidUserDetailsException;
import com.bigdecimal.clasnapp.user.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    ObjectMapper mapper = new ObjectMapper();
    try {
      LoginDto loginDto = mapper.readValue(request.getReader(), LoginDto.class);
      return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          loginDto.username(),
          loginDto.password()
        )
      );
    } catch (IOException e) {
      throw new InvalidUserDetailsException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    log.info("successfulAuthentication(). Authentication was successful.");
    User user = (User) authResult.getPrincipal();
    response.setContentType("application/json;charset=utf-8");
    String accessToken = jwtService.accessToken(user);
    try (PrintWriter out = response.getWriter()) {
      out.print(String.format("{\"accessToken\":\"%s\"}", accessToken));
    } catch (Exception e) {
      log.error(
        "failed to print object into respone body because {}",
        e.getCause()
      );
      response.setHeader("access_token", accessToken);
    }
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  ) throws IOException, ServletException {
    log.error("Authentication failed.");
  }
}
