package com.example.softwareProjectWithDocker.jwtTest;


import com.example.softwareProjectWithDocker.security.jwt.JwtConfig;
import com.example.softwareProjectWithDocker.security.jwt.JwtVerifier;
import jakarta.servlet.FilterChain;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class JwtTest {
    
    @InjectMocks
    private JwtVerifier jwtVerifier;

    @Mock
    private JwtConfig jwtConfig;
    
    @Mock
    private FilterChain filterChain;

    @Test
    void testValidJwtTokenSetsAuthentication() throws Exception {
        // Arrange                  // Header           // Payload
        String validToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJOZW8iLCJBdXRob3JpdGllcyI6W3siYXV0aG9yaXR5Ijoic291cmNlOndyaXRlIn0seyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJzb3VyY2U6cmVhZCJ9XSwiaWF0IjoxNzQ4Mjc4ODAwLCJpc3MiOiIvYXBpL3YxL2xvZ2luIiwiZXhwIjoxNzQ4ODgzNjAwfQ.EDFG1kpQaqi2SHMOOhwkDeqKgYPzRTQ-bvQ800cyoCNsNHLICsQxFGcpaUA2rElYiD6evHBh9EVjhGJ6Q0MCRA";
        when(jwtConfig.getAccessHeader()).thenReturn("Bearer ");
        when(jwtConfig.getHeader()).thenReturn(jwtConfig.getTokenPrefix());

        // Mock request with a valid JWT token
        var request = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        var response = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletResponse.class);
        when(request.getServletPath()).thenReturn("/api/v1/login");
        when(request.getHeader(jwtConfig.getHeader())).thenReturn(validToken);

        // Act & Assert
        jwtVerifier.doFilter(request, response, filterChain);
        org.mockito.Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void testInvalidJwtTokenReturnsErrorResponse() throws Exception {

        // Arrange
        String invalidToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJOZW8iLCJBdXRob3JpdGllcyI6W3siYXV0aG9yaXR5Ijoic291cmNlOndyaXRlIn0seyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJzb3VyY2U6cmVhZCJ9XSwiaWF0IjoxNzQ4Mjc4ODAwLCJpc3MiOiIvYXBpL3YxL2xvZ2luIiwiZXhwIjoxNzQ4ODgzNjAwfQ.EDFG1kpQaqi2SHMOOhwkDeqKgYPzRTQ-bvQ800cyoCNsNHLICsQxFGcpaUA2rElYiD6evHBh9EVjhGJ6Q0MCRA";
        when(jwtConfig.getAccessHeader()).thenReturn(jwtConfig.getHeader());
        when(jwtConfig.getHeader()).thenReturn(jwtConfig.getTokenPrefix());

        // Mock request with an invalid JWT token
        var request = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        var response = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletResponse.class);
        when(request.getServletPath()).thenReturn("/api/v1/login");
        when(request.getHeader(jwtConfig.getHeader())).thenReturn(invalidToken);

        var errorStream = new java.io.ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public void write(int b) {
                errorStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
            }
        });

        // Act
        jwtVerifier.doFilter(request, response, filterChain);

        org.mockito.Mockito.verify(response);
        String errorContent = errorStream.toString();
        assert errorContent.contains("error");
    }
}