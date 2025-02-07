package com.dangquocdat.FoodOrdering.config;

import com.dangquocdat.FoodOrdering.security.jwt.JwtAuthenticationEntryPoint;
import com.dangquocdat.FoodOrdering.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.requestMatchers("/api/restaurants/*/favorites").authenticated();
                    authorize.requestMatchers("/api/restaurants/**", "/api/categories/**", "/api/foods/**").permitAll();
                    authorize.requestMatchers("/api/**").authenticated();
                    authorize.anyRequest().permitAll();
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration=new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                configuration.setAllowedMethods(Arrays.asList("*"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList("Authorization")); // allow the client (FE) to access the "Authorization" header
                configuration.setMaxAge(3600L); // 1 hour : browser to cache this response for 1 hour,
                                                // which reduces the number of preflight requests and improves performance for subsequent requests.
                return configuration;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
