package com.example.softwareProjectWithDocker.security.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String tokenPrefix;
    private Long tokenExpiration;
    private String secretKey;

    private String accessHeader;

    private String refreshHeader;

    public String getSecretKey() {
        return secretKey;
    }

    public String getAccessHeader() {
        return accessHeader;
    }


    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getRefreshHeader() {
        return refreshHeader;
    }

    public Long getTokenExpiration() {
        return tokenExpiration;
    }


    public String getHeader(){
        return HttpHeaders.AUTHORIZATION;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public void setAccessHeader(String accessHeader) {
        this.accessHeader = accessHeader;
    }

    public void setRefreshHeader(String refreshHeader) {
        this.refreshHeader = refreshHeader;
    }
}
