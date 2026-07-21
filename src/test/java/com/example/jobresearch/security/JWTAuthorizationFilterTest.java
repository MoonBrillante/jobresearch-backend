package com.example.jobresearch.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JWTAuthorizationFilterTest {

    private static final String TEST_SECRET = "test-secret-key-for-jwt-unit-tests-12345";

    private JWTAuthorizationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JWTAuthorizationFilter();
        ReflectionTestUtils.setField(filter, "SECRET_KEY", TEST_SECRET);
        // Make sure no leftover authentication from a previous test affects this one
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Avoid leaking authentication state into other test classes
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws Exception {
        // Arrange
        String token = generateToken("testuser");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/jobs");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("testuser", authentication.getPrincipal());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_missingToken_doesNotAuthenticateButStillProceeds() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/jobs");
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "No token means no authentication should be set");
        verify(chain).doFilter(request, response);
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_USER"))
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000L))
                .signWith(SignatureAlgorithm.HS256, TEST_SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}