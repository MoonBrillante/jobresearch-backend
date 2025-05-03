package com.example.jobresearch.config;

import com.example.jobresearch.security.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    private final JWTAuthorizationFilter jwtAuthorizationFilter;


    public SecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS config
                .csrf(csrf -> csrf.disable())  // Disable CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //Stateless session
                .formLogin(form -> form.disable())    // Disable Spring Security's default form login functionality.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()  // Allow everyone to access /login
                        .requestMatchers(HttpMethod.GET, "/api/jobs").hasAnyRole("USER", "ADMIN")  // Protect /api/jobs
                        .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasRole("ADMIN")           // Only ADMIN can add new jobs
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("ADMIN")            // Only ADMIN can edit job
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("ADMIN")         // Only ADMIN can delete job
                        .requestMatchers(HttpMethod.POST, "/api/import").permitAll()
                        .anyRequest().authenticated()  // Other requests require authentication
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWt filter to filter chain

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use  BCrypt password encryption
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String allowedOrigin = System.getenv().getOrDefault("FRONTEND_ORIGIN", "http://localhost:5173");
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization"); // Add this line to ensure that the front end can get the Authorization header
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        System.out.println("CORS for: " + configuration.getAllowedOrigins());
        System.out.println("Allowed methods: " + configuration.getAllowedMethods());
        return source;

    }


}

