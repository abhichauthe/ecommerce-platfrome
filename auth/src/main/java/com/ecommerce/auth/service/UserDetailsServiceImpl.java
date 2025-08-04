package com.ecommerce.auth.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        switch (username) {
            case "admin":
                return createUser("admin", "password", "ROLE_ADMIN", "ROLE_USER");

            case "user":
                return createUser("user", "password", "ROLE_USER");

            case "manager":
                return createUser("manager", "password", "ROLE_ADMIN", "ROLE_USER");

            default:
                throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private UserDetails createUser(String username, String password, String... roles) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(username, passwordEncoder.encode(password), authorities);
    }
}