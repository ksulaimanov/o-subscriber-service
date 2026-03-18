package kg.nurtelecom.o_subscriber_service.config;

import kg.nurtelecom.o_subscriber_service.security.CustomUserDetailsService;
import kg.nurtelecom.o_subscriber_service.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository cookieCsrfTokenRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        http
                .userDetailsService(customUserDetailsService)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(cookieCsrfTokenRepository)
                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/api/auth/login")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/images/**",
                                "/photos/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/register").permitAll()

                        .requestMatchers("/home", "/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/profile/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/subscribers/*/photo").authenticated()
                        .requestMatchers(HttpMethod.POST, "/subscribers/*/photo").authenticated()
                        .requestMatchers(HttpMethod.POST, "/subscribers/*/photo/delete").authenticated()
                        .requestMatchers(HttpMethod.POST, "/subscribers/*/email").authenticated()
                        .requestMatchers(HttpMethod.POST, "/subscribers/*/tariff").authenticated()

                        .requestMatchers("/subscribers-page").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/subscribers", "/api/subscribers/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/subscribers/dao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/subscribers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/subscribers/*/photo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/subscribers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/subscribers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/subscribers/**").hasRole("ADMIN")

                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .httpBasic(httpBasic -> {})
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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