package com.example.demo.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.configs.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,  @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(SecurityConfig.LOGIN_PATH) || request.getServletPath().equals(SecurityConfig.TOKEN_REFRESH_PATH) || request.getServletPath().equals(SecurityConfig.REGISTER_PATH)) {
            log.debug("Try to generate new auth header");
            filterChain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.debug("Auth header = {}", authHeader);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    String token = authHeader.substring("Bearer " .length());
                    JWTVerifier verifier = JWT.require(CustomFilter.algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    Collection<GrantedAuthority> authorities = decodedJWT.getClaim("roles").asList(String.class).stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.debug("Auth header is wrong. Reason={}", e.getMessage());
                    response.setHeader("Error", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    Map<String, String> errorMessages = new HashMap<>();
                    errorMessages.put("Error", e.getClass().getSimpleName());
                    errorMessages.put("Reason", e.getMessage());
                    new ObjectMapper().writeValue(response.getWriter(), errorMessages);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
