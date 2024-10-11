package com.assignment.portal.util;
import com.assignment.portal.model.UserDetailsEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {
    private final byte[] SECRET_KEY_BYTES = "D3Z3gA3fXsU1T5byE4g8c1r9MZLrD+PTz+o38AB4wX8I=".getBytes();
    private final SecretKeySpec SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, SignatureAlgorithm.HS256.getJcaName());
    private final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(UserDetailsEntity user) {
        return Jwts.builder()
                .setSubject(user.getUserMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
