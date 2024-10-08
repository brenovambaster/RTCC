package com.rtcc.demo.services;

import com.rtcc.demo.infra.config.UserAuthenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;


@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Long expiry = 3600L;

        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        UserAuthenticated userAuthenticated = (UserAuthenticated) authentication.getPrincipal();

        String name = userAuthenticated.getUser().getName();
        String userId = userAuthenticated.getUser().getId();


        var claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .claim("user_id", userId)
                .claim("name", name)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
