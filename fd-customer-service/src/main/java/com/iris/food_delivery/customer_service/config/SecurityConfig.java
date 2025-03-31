package com.iris.food_delivery.customer_service.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iris.food_delivery.customer_service.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        	.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable custom CORS config
	            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/customer/ping").permitAll() // Public endpoints
	                .anyRequest().authenticated() // All other endpoints require authentication
	            	//.anyRequest().permitAll()
	            )
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Ensures no session is stored
	            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); // JWT authentication

	        return http.build();
	    }
	
    @Bean
    JwtFilter jwtFilter() {
        return new JwtFilter();
    }
    
 // Global CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); // Allow all origins
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*")); // Allow all headers including Authorization
        config.setExposedHeaders(Arrays.asList("Authorization")); // Expose token header
        config.setAllowCredentials(true); // Required if credentials (like cookies or auth headers) are sent

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

