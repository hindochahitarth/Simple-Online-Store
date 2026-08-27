package org.example.simpleonlinestore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity

public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider,
                                 JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf(csrf -> csrf.disable());// turns off csrf
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));// links custom origin rules to
        // match frontend with backend
        // opens rules who can access which url path
        httpSecurity.authorizeHttpRequests(auth -> auth

                .requestMatchers("/auth/**").permitAll()// allow anyone to access url with /auth
                .requestMatchers("/error").permitAll()
                .anyRequest()// for every single url
                .authenticated());// user must be logged in
        httpSecurity.sessionManagement(session -> session

                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))// never to create session
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// filter jwt
        // first

        return httpSecurity.build();// locks in all settings and configurations
    }

    // control which external ports can access backend
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();// settings object
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));// frontend running o n this
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));// puts jwt token frontend send token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);// allow to every single endpooint match any path and all sub-paths
        return source;
    }

}
