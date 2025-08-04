package com.ecommerce.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.ecommerce.auth")
@ConfigurationProperties(prefix = "ecommerce.auth")
public class AuthConfiguration {

    private String jwtSecret = "ThisIsAVerySecureSecretKeyForJWTTokenGenerationThatIsAtLeast32BytesLongAndSecureEnoughForHS512Algorithm";
    private Long jwtExpiration = 86400000L; // 24 hours


    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public void setJwtExpiration(Long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }
}