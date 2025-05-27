package com.example.softwareProjectWithDocker.security.jwt;

import com.example.softwareProjectWithDocker.security.UserAppDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final Logger auth_logger = LoggerFactory.getLogger(JwtUsernameAndPasswordAuthenticationFilter.class);

    private final JwtConfig jwtConfig;
    private final SecretKey jwtSecretKey;

    @Autowired
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey jwtSecretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Autowired
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // to explicitly convert data from user request into JwtUsernameAndPasswordRequest obj to make it as JSON object
            JwtUsernameAndPasswordRequest jwtUsernameAndPasswordRequest = new ObjectMapper().readValue(request.getInputStream(), JwtUsernameAndPasswordRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUsernameAndPasswordRequest.getUsername(), jwtUsernameAndPasswordRequest.getPassword());
            auth_logger.info("User {} is trying to login", authentication.getName());
            auth_logger.info("User {} is trying to login with password {}", authentication.getName(), authentication.getCredentials());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            auth_logger.error("Error while parsing user request", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {

        UserAppDetail appDetail = (UserAppDetail) authentication.getPrincipal();

        auth_logger.info("User {} logged in successfully", appDetail.getUsername());

        String access_token = Jwts.builder()
                                .subject(appDetail.getUsername())
                                .claim("Authorities",appDetail.getAuthorities())
                                .issuedAt(java.sql.Date.valueOf(LocalDate.now()))
                                .issuer(request.getRequestURI().toString())
                                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                                .signWith(jwtSecretKey)
                                .compact();

        String refresh_token = Jwts.builder()
                                       .subject(appDetail.getUsername())
                                       .claim("Authorities",appDetail.getAuthorities())
                                       .issuedAt(java.sql.Date.valueOf(LocalDate.now()))
                                       .issuer(request.getRequestURI().toString())
                                       .expiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiration())))
                                       .signWith(jwtSecretKey)
                                       .compact();

        response.addHeader(jwtConfig.getAccessHeader(), jwtConfig.getTokenPrefix() + access_token);
        response.addHeader(jwtConfig.getRefreshHeader(), jwtConfig.getTokenPrefix() + refresh_token);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();


        Map<String,Object> responses = new HashMap<>();
        responses.put("access token",access_token);
        responses.put("refresh token",refresh_token);
        responses.put("username",appDetail.getUsername());
        responses.put("count",responses.size());

        new ObjectMapper().writeValue(response.getOutputStream(), responses);
    }

}

