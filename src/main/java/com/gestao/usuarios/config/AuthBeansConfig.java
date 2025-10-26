package com.gestao.usuarios.config;

import com.gestao.usuarios.user.User;
import com.gestao.usuarios.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@Configuration
public class AuthBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repo) {
        return username -> {
            User u = repo.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var authorities = u.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());

            return org.springframework.security.core.userdetails.User
                    .withUsername(u.getEmail())
                    .password(u.getPasswordHash())
                    .authorities(authorities)
                    .accountLocked(!u.isActive())
                    .disabled(!u.isActive())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
