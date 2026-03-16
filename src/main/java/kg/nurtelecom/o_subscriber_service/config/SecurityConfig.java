package kg.nurtelecom.o_subscriber_service.config;

import kg.nurtelecom.o_subscriber_service.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/subscribers", "/api/subscribers/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/subscribers/dao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/subscribers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/subscribers/*/photo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/subscribers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/subscribers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/subscribers/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}