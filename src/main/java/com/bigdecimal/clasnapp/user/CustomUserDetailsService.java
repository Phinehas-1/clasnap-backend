package com.bigdecimal.clasnapp.user;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        String password = user.getPassword();    
        List<String> roles = user.getRoles().stream().map(role -> role.getName().name()).toList();
        return new org.springframework.security.core.userdetails.User(username, password,
                roles.stream().map(SimpleGrantedAuthority::new).toList());
    }

}
