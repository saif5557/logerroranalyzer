package com.saif.logerroranalyzer.config;

import com.saif.logerroranalyzer.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Admin Dashboard
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Error Code Management API
                        // Read Access: Any Authenticated User
                        .requestMatchers(HttpMethod.GET, "/api/error-codes/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/error-codes").authenticated()

                        // Write Access: ADMIN Only
                        .requestMatchers(HttpMethod.POST, "/api/error-codes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/error-codes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/error-codes/**").hasRole("ADMIN")

                        // Error Code UI Page (Viewable by any logged in user)
                        .requestMatchers("/error-codes").authenticated()

                        // Everything else is public
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/error-codes", true) // Redirect to the protected area after login
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}
