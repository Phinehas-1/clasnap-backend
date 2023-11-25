package com.bigdecimal.clasnapp.auth.config;

import com.bigdecimal.clasnapp.domain.user.data.User;
import com.bigdecimal.clasnapp.domain.user.data.UserRepository;
import com.bigdecimal.clasnapp.security.service.JwtService;

import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
@RequiredArgsConstructor
public class AuthConfig {

  private final AuthorizationFilter authorizationFilter;
  private final JwtService jwtService;
  private final UserRepository users;

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
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
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

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      User user = users
        .findByUsername(username)
        .orElseThrow(EntityNotFoundException::new);
      String password = user.getPassword();
      List<String> roles = user
        .getRoles()
        .stream()
        .map(role -> role.getName().name())
        .toList();
      return new org.springframework.security.core.userdetails.User(
        username,
        password,
        roles.stream().map(SimpleGrantedAuthority::new).toList()
      );
    };
  }
}
