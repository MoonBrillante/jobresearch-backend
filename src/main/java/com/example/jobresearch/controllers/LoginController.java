package com.example.jobresearch.controllers;

import com.example.jobresearch.domain.models.AccountCredentials;
import com.example.jobresearch.security.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    /*public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        UsernamePasswordAuthenticationToken creds =
                new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());

        Authentication auth = authenticationManager.authenticate(creds);

        // Get the user's role information
        //List<GrantedAuthority> roles = (List<GrantedAuthority>) auth.getAuthorities();
        List<GrantedAuthority> roles = new ArrayList<>(auth.getAuthorities());


        // Generate a JWT token with role information
        String jwts = jwtService.getToken(auth.getName(), roles);

        // Build the response with the JWT token
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }

}*/
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        try {
            UsernamePasswordAuthenticationToken creds =
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());

            Authentication auth = authenticationManager.authenticate(creds);

            List<GrantedAuthority> roles = new ArrayList<>(auth.getAuthorities());

            String jwts = jwtService.getToken(auth.getName(), roles);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .build();
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}