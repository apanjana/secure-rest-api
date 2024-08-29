package com.ust.securerestapi.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class JwtService {

    private final String SECRET_KEY = "36F4A8EB4B18CAC332F9DBA5DC1E1775D6EB45BCDD6F9F90B741791B65724758";
    private final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(30); // 30 minutes

    public String generateToken(UserDetails userDetails){
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", "SecureAPI");
        claims.put("sub", "Secure API Access");
        claims.put("aud", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().toString());
        String token =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .signWith(generateKey())
                .compact();
        return token;
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
