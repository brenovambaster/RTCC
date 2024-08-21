package com.rtcc.demo.security.JWT;

import com.rtcc.demo.services.UserDetailImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${project.jwtSecret}")
    private String jwtSecret;

    @Value("${project.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String genereateTokenFromUserDetailsImpl(UserDetailImpl userData) {
        return Jwts.builder()
                .subject(userData.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey()) // O algoritmo é inferido a partir da chave
                .compact();
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(
                getSigningKey()).build().
                parseClaimsJws(token).
                getBody().getSubject();
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid Token" + e.getMessage());
        } catch (ExpiredJwtException e){
            System.out.println("Expired Token" + e.getMessage());
        } catch (UnsupportedJwtException e){
            System.out.println("Unsupported Token" + e.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println("Invalid Argument" + e.getMessage());
        }
        return false;
    }
}
