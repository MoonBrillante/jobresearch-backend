package com.example.jobresearch.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")  // Read the secret key from the configuration file
    private String SECRET_KEY;

    // Define Bearer prefix constant
    private static final String PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/login".equals(path) || "/health".equals(path)) {
            // Login and health check interfaces are directly released
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);  // Remove Bearer prefix

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))  // Decode using key
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))  // Remove "Bearer " prefix
                    .getBody();

            System.out.println("JWT Claims: " + claims);
            System.out.println("JWT Token Subject: " + claims.getSubject());

            String username = claims.getSubject();
            List<SimpleGrantedAuthority> authorities = Collections.emptyList();

            // Extract role information
            if (claims.get("roles") != null) {
                authorities = ((List<?>) claims.get("roles")).stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
                        .collect(Collectors.toList());
            }

            // Verify and set user information
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            System.out.println("JWT Secret Key: " + SECRET_KEY);
            System.out.println("JWT Token: " + token);
            System.out.println("JWT filter reached path: " + request.getRequestURI());
        }


        chain.doFilter(request, response);
    }
}
