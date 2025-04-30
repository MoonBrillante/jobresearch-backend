package com.example.jobresearch.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // Read the key from application.properties or application.yml
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    static final long EXPIRATIONTIME = 86400000; // 1 day, can be shortened in production environment
    static final String PREFIX = "Bearer";

    public String getToken(String username, List<GrantedAuthority> roles) {
        // Convert GrantedAuthority to role name string
        List<String> roleNames = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Generate a JWT token and store it in a Variable
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)  // Add role information to JWT's clamims
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))  // Set expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))  // Sign with secret key
                .compact();

        // Print the generated JWT token for debugging
        System.out.println("Generated JWT Token: " + token);

        // Return the generated token
        return token;
    }


    // verify the token, and get username
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(PREFIX)) {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))  // Use the same key to verify
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))  // delete "Bearer " prefix
                    .getBody()
                    .getSubject();  // Get the username in the JWT
        }
        return null;
    }
}
