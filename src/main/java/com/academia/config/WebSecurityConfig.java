package com.academia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.academia.service.impl.LoginServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
    @Autowired
    private LoginServiceImpl loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public static final String[] ENDPOINTS_WHITELIST = {
            "/css/**", 
            "/js/**", 
            "/assets/**", 
            "/",
            "/login"
    };

    public static final String[] ENDPOINTS_ADMIN = {
    		"/usuarios",
            "/v1/usuarios/**",
            "/carrera",
            "/curso",
            "/docente",
    };

    public static final String[] ENDPOINTS_AS = {
    		"/alumno",
    		"/docAsistencia",
    		"/aluAsistencia",
            "/dashboard",
            "/acercaDe"
    };

    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/dashboard";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                .requestMatchers(ENDPOINTS_WHITELIST).permitAll() 
                .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMIN") 
                .requestMatchers(ENDPOINTS_AS).hasAnyRole("ADMIN", "SECRETARIO") 
                .anyRequest().authenticated() 
            )
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .failureUrl(LOGIN_FAIL_URL)
                .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
            )
            .logout(logout -> logout
                .logoutUrl(LOGOUT_URL)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(LOGIN_URL + "?logout")
            );
        return http.build();
    }
}
