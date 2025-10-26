// src/main/java/com/gestao/usuarios/config/SecurityConfig.java
package com.gestao.usuarios.config;

import com.gestao.usuarios.security.JwtAuthFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(AuthenticationProvider authProvider, JwtAuthFilter jwtAuthFilter) {
        this.authProvider = authProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain actuatorChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(EndpointRequest.to("health")).permitAll()
                        .requestMatchers(EndpointRequest.to("info")).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // pré-voo CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger / OpenAPI (liberamos AMBOS os caminhos)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()

                        // públicos
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/ping").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/forgot").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/reset").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/auth/reset").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
