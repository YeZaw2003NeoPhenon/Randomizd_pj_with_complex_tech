package com.example.softwareProjectWithDocker.security.jwt;

import com.example.softwareProjectWithDocker.response.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtConfig jwtConfig;
    private final SecretKey jwtSecretKey;

    @Autowired
    public JwtController(JwtConfig jwtConfig, SecretKey jwtSecretKey) {
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Object>> generateRefreshToken(HttpServletResponse response, HttpServletRequest request){

        String refreshHeader = request.getHeader(jwtConfig.getRefreshHeader());

        if(refreshHeader == null || !refreshHeader.startsWith(jwtConfig.getTokenPrefix())){
            return ResponseEntity.badRequest().body(ApiResponse.error(null, "No refresh token provided"));
        }

        String refresh_token = refreshHeader.replace(jwtConfig.getTokenPrefix(), "");

     Jws<Claims> claimsJws=   Jwts.parser()
                                  .verifyWith(jwtSecretKey)
                                  .build()
                                  .parseSignedClaims(refresh_token);

     Claims claim = claimsJws.getPayload();

     String username = claim.getSubject();

     String newAccessToken = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(jwtSecretKey)
                .compact();

        String newRefreshToken = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiration())))
                .signWith(jwtSecretKey)
                .compact();

        Map<String,String> responses = new HashMap<>();
        responses.put("access token",newAccessToken);
        responses.put("refresh token",newRefreshToken);

        return ResponseEntity.ok(ApiResponse.success(responses, "List of tokens implemented"));
    }
}