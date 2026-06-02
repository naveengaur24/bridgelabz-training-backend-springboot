package com.fundoonotes.fundoo_notes.config;
import com.fundoonotes.fundoo_notes.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

// Security config define karta hai application ko secure kaise karna hai
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // CORS Configuration Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration =
                new CorsConfiguration();

        // Frontend URL allow kiya
        configuration.setAllowedOrigins(
                List.of("http://localhost:5174"));

        // Saare HTTP methods allow kiya
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT",
                        "DELETE", "OPTIONS"));

        // Saare headers allow kiyeee
        configuration.setAllowedHeaders(
                List.of("*"));

        // Authorization header expose kiya
        configuration.setExposedHeaders(
                List.of("Authorization"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Saare URLs pe CORS apply kia
        source.registerCorsConfiguration(
                "/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // CORS enable karo — React se calls allow
                .cors(cors -> cors
                        .configurationSource(
                                corsConfigurationSource()))

                // CSRF disable — REST API stateless hai
                .csrf(csrf -> csrf.disable())

                // Session nhi banana bcz — JWT stateless hai
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // Exception handling
                .exceptionHandling(exception -> exception

                        // Bina token → 401
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setStatus(401);
                                    response.setContentType(
                                            "application/json");
                                    response.getWriter().write(
                                            "{\"success\":false," +
                                                    "\"message\":\"Unauthorized!" +
                                                    " Please login first.\"," +
                                                    "\"data\":null}");
                                })

                        // Token hai but permission nahi → 403
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    response.setStatus(403);
                                    response.setContentType(
                                            "application/json");
                                    response.getWriter().write(
                                            "{\"success\":false," +
                                                    "\"message\":\"Access Denied!\"," +
                                                    "\"data\":null}");
                                })
                )

                // URL permissions
                .authorizeHttpRequests(auth -> auth

                        // Ye URLs bina token ke allow hain
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/forgot-password",
                                "/api/users/verify-otp",
                                "/api/users/reset-password"
                        ).permitAll()

                        // Baaki sab URLs token chahiye
                        .anyRequest().authenticated()
                )

                // JwtFilter pehle run hoga
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}