package com.pvz.demo2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // включает @PreAuthorize в контроллерах
@RequiredArgsConstructor
public class SecurityConfig {

    private final com.pvz.demo.security.CustomUserDetailsService userDetailsService;

    // 1. BCrypt — так пароли хранятся в хешированном виде
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Провайдер аутентификации: берёт юзера из БД, сверяет пароль через BCrypt
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsPasswordService((UserDetailsPasswordService) userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 3. Главные правила: кто какие URL видит
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // --- Публичные страницы (гость тоже видит) ---
                        .requestMatchers(
                                "/", "/catalog", "/catalog/**",
                                "/contacts", "/faq",
                                "/login", "/register",
                                "/css/**", "/js/**", "/images/**",
                                "/h2-console/**"
                        ).permitAll()

                        // --- Только админ ---
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // --- Только залогиненные (USER или ADMIN) ---
                        .requestMatchers("/profile/**", "/requests/**")
                        .hasAnyRole("USER", "ADMIN")

                        // --- Всё остальное — только аутентифицированным ---
                        .anyRequest().authenticated()
                )
                // --- Форма входа ---
                .formLogin(form -> form
                        .loginPage("/login")               // своя страница
                        .loginProcessingUrl("/login")      // куда уходит POST формы
                        .defaultSuccessUrl("/", true)      // после входа
                        .failureUrl("/login?error=true")   // при ошибке
                        .permitAll()
                )
                // --- Выход ---
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .permitAll()
                )
                // --- H2 консоль (для отладки) ---
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}