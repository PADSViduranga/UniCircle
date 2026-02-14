package com.example.UniCricle.config;

import com.example.UniCricle.Auth.CustomLoginFailureHandler;
import com.example.UniCricle.Auth.CustomLoginSuccessHandler;
import com.example.UniCricle.Auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomLoginFailureHandler failureHandler;
    private final CustomLoginSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // These paths bypass the security filter chain entirely
        return (web) -> web.ignoring().requestMatchers(
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",
                "/login.css",
                "/index.css",
                "/register.css",
                "/admin/**" // Note: Usually we don't ignore admin, but keep it if your CSS is inside there
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC ACCESS (Updated to include About and Contact)
                        .requestMatchers("/", "/login", "/register", "/about", "/contact", "/clubs/view/**").permitAll()

                        // 2. ADMIN ONLY ACCESS
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/announcements/admin/**").hasRole("ADMIN")

                        // 3. LEADER / CLUB MANAGEMENT ACCESS
                        .requestMatchers("/leader/**").hasAnyRole("PRESIDENT", "VICE_PRESIDENT", "CLUB_LEADER")
                        .requestMatchers("/announcements/create").hasAnyRole("ADMIN", "PRESIDENT", "VICE_PRESIDENT", "CLUB_LEADER")

                        // 4. GENERAL USER ACCESS
                        .requestMatchers("/user/**", "/dashboard/**", "/charity/**").hasAnyRole("USER", "ADMIN", "PRESIDENT", "VICE_PRESIDENT")
                        .requestMatchers("/clubs/**").hasAnyRole("USER", "PRESIDENT", "VICE_PRESIDENT", "ADMIN")

                        // 5. CATCH-ALL
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(failureHandler)
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }
}