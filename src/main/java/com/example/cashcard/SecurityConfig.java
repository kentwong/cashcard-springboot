package com.example.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// The @Configuration annotation tells Spring to use this class to configure Spring and Spring Boot
// itself. Any Beans specified in this class will now be available to Spring's Auto Configuration
// engine.
@Configuration
class SecurityConfig {

    // Spring Security expects a Bean to configure its Filter Chain
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // All HTTP requests to cashcards/ endpoints are required to be authenticated using HTTP
        // Basic Authentication security (username and password).
        // Also, do not require CSRF security.
        http
            .authorizeHttpRequests(request -> request
                .requestMatchers("/cashcards/**")
                .hasRole("CARD-OWNER"))
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring's IoC container will find the UserDetailsService Bean and Spring Data will use it when
    // needed.
    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
            .username("sarah1")
            .password(passwordEncoder.encode("abc123"))
            .roles("CARD-OWNER")
            .build();
        UserDetails hankOwnsNoCards = users
            .username("hank-owns-no-cards")
            .password(passwordEncoder.encode("qrs456"))
            .roles("NON-OWNER") // new role
            .build();
        return new InMemoryUserDetailsManager(sarah, hankOwnsNoCards);
    }
}
