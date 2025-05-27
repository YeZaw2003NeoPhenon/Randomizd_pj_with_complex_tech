package com.example.softwareProjectWithDocker.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class JwtVerifier extends OncePerRequestFilter {
    private final SecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtVerifier(SecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/api/v1/login")){
            filterChain.doFilter(request, response);
            return;
        }

        // fixed one error here
        String authorizationHeader = request.getHeader(jwtConfig.getHeader());

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try{
            Jws<Claims> claims = Jwts.parser()
                                    .verifyWith(jwtSecretKey)
                                    .build()
                                    .parseSignedClaims(token);

            Claims claim = claims.getPayload();
            String username = claim.getSubject();

            var authorities = (List<Map<String,String>>) claim.get("Authorities");

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                                                                        .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                                                                        .collect(Collectors.toSet());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            authenticationToken.setDetails(claims);
//            authenticationToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        catch(JwtException e){
            response.setHeader("error", e.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
