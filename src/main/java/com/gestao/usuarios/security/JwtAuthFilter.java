package com.gestao.usuarios.security;

import com.gestao.usuarios.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final UserRepository repo;

    public JwtAuthFilter(JwtService jwt, UserRepository repo) {
        this.jwt = jwt;
        this.repo = repo;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;     // CORS preflight
        if ("/auth/login".equals(path)) return true;
        if ("/ping".equals(path)) return true;
        if ("/error".equals(path)) return true;
        if (path.startsWith("/actuator")) return true;                        // actuator já tem chain próprio
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader("Authorization");
        if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        if (!jwt.isValid(token)) {
            chain.doFilter(request, response);
            return;
        }

        String email = jwt.extractSubject(token);
        var optUser = repo.findByEmailIgnoreCase(email);
        if (optUser.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        var u = optUser.get();

        List<SimpleGrantedAuthority> authorities = u.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        var authToken = new UsernamePasswordAuthenticationToken(u, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(request, response);
    }
}
