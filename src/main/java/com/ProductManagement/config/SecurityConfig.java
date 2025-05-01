package com.ProductManagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String SECRET_KEY = "a7c78c15ef0b4dc9814efb7f5f28a9d4c3e57f9bffab4d8e8c02e7a981c78c15a7c78c15ef0b4dc9814efb7f5f28a9d4c3e57f9bffab4d8e8c02e7a981c78c15";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of(role))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/products/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    static class JwtTokenFilter extends org.springframework.web.filter.OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = Jwts
                            .parser()
                            .setSigningKey(SECRET_KEY.getBytes())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String username = claims.getSubject();
                    List<String> roles = claims.get("roles", List.class);
                    var authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception ignored) {
                    System.err.println("Error parsing JWT token: " + ignored.getMessage());

                }
            }
            filterChain.doFilter(request, response);
        }
    }

}