package com.example.jobresearch.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    // Must be long enough for HS256 (at least 32 bytes recommended)
    private static final String TEST_SECRET = "test-secret-key-for-jwt-unit-tests-12345";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // @Value fields are not injected outside a Spring context,
        // so we set the private field directly for testing purposes.
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", TEST_SECRET);
    }

    @Test
    void getToken_containsCorrectSubject() {
        // Arrange
        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // Act
        String token = jwtService.getToken("testuser", roles);

        // Assert
        Claims claims = parseClaims(token);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void getToken_containsRolesClaim() {
        // Arrange
        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        // Act
        String token = jwtService.getToken("testuser", roles);

        // Assert
        Claims claims = parseClaims(token);
        List<?> roleNames = claims.get("roles", List.class);
        assertTrue(roleNames.contains("ROLE_USER"));
        assertTrue(roleNames.contains("ROLE_ADMIN"));
    }

    @Test
    void getToken_hasExpirationApproximatelyOneDayInFuture() {
        // Arrange
        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        long beforeGeneration = System.currentTimeMillis();

        // Act
        String token = jwtService.getToken("testuser", roles);

        // Assert
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();

        long expectedExpiration = beforeGeneration + 86_400_000L; // 24 hours in ms
        long diff = Math.abs(expiration.getTime() - expectedExpiration);

        // Allow a few seconds of tolerance for test execution time
        assertTrue(diff < 5000, "Expiration should be ~24 hours from generation time");
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TEST_SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}