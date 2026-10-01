package org.example.simpleonlinestore.config;

import org.example.simpleonlinestore.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // during login
        return username -> {

            return userRepository.findByEmailId(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User missing in database for: " + username));
        };
    }   
    //bcrypt proivdes one way hashing ,uses salt automatically
    //salt:-random data added
    //Blowfish algorithm
//    BCrypt extracts salt & cost from stored hash
//    Re-hashes entered password with same parameters
//    Compares the result
    @Bean
    BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();//default strength is 10
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
