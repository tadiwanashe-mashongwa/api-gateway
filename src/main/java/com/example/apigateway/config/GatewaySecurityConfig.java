package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.util.List;

@Configuration
public class GatewaySecurityConfig {
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/health", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui", "/swagger-ui/**", "/api/parts/**", "/api/brands/**", "/api/categories/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${cors.allowed-origin:http://localhost:3000}") String allowedOrigin
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Location"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    WebFilter frontendCorsFilter(
            @Value("${cors.allowed-origin:http://localhost:3000}") String allowedOrigin
    ) {
        return (ServerWebExchange exchange, org.springframework.web.server.WebFilterChain chain) -> {
            String origin = exchange.getRequest().getHeaders().getOrigin();
            if (allowedOrigin.equals(origin)) {
                HttpHeaders headers = exchange.getResponse().getHeaders();
                headers.setAccessControlAllowOrigin(allowedOrigin);
                headers.setAccessControlAllowCredentials(true);
                headers.setAccessControlAllowMethods(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS));
                headers.setAccessControlAllowHeaders(List.of("*"));
                headers.setAccessControlExposeHeaders(List.of(HttpHeaders.LOCATION));
                if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
    }
}
