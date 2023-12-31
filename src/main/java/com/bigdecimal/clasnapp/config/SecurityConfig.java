package com.bigdecimal.clasnapp.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bigdecimal.clasnapp.auth.AuthenticationFilter;
import com.bigdecimal.clasnapp.auth.AuthorizationFilter;
import com.bigdecimal.clasnapp.util.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthorizationFilter authorizationFilter;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security)
    throws Exception {
    log.info("Setting up security..");
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(
      authenticationManager(),
      jwtService
    );
    authenticationFilter.setFilterProcessesUrl("/auth/signin");
    security.csrf(csrfConfig -> csrfConfig.disable());
    security.sessionManagement(sessionConfig ->
      sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
    security.authorizeHttpRequests(authManager -> {
      authManager
        .requestMatchers(
          "/admin/**",
          "/auth/**",
          "/v3/api-docs",
          "/v3/api-docs/**",
          "/swagger-resources/configuration/ui",
          "/swagger-resources",
          "/swagger-resources/configuration/security",
          "/swagger-ui.html",
          "/swagger-ui/**",
          "/webjars/**"
        )
        .permitAll();
      authManager.anyRequest().authenticated();
    });
    security.addFilterAt(
      authenticationFilter,
      UsernamePasswordAuthenticationFilter.class
    );
    security.addFilterBefore(
      authorizationFilter,
      UsernamePasswordAuthenticationFilter.class
    );
    security.cors(customizer -> {
      customizer.configurationSource(corsConfigurationSource());
    });
    return security.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(CorsConfiguration.ALL));
    configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
    configuration.setMaxAge(Duration.ofMinutes(60));
    configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
    configuration.setExposedHeaders(Arrays.asList(CorsConfiguration.ALL));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Arrays.asList(authenticationProvider()));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
