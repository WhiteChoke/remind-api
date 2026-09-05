package com.whitechoke.remind.security;

import com.whitechoke.remind.security.refreshToken.RefreshTokenEntity;
import com.whitechoke.remind.security.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.access-expiry-ms}")
    private int accessExpiryMs;
    @Value("${jwt.refresh-expiry-ms}")
    private long refreshExpiryMs;
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(JwtUserDetails userDetails) {
        var claims = new HashMap<String, Object>();

        claims.put("id", userDetails.getId());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiryMs))
                .signWith(getSigningKey())
                .compact();
    }

    @Transactional
    public String generateRefreshToken(JwtUserDetails userDetails) {
        refreshTokenRepository.deleteByUserId(userDetails.getId());

        var token = UUID.randomUUID().toString();

        refreshTokenRepository.save(
                RefreshTokenEntity.builder()
                        .token(token)
                        .expiresAt(Instant.now().plusMillis(refreshExpiryMs))
                        .user(userDetails.getUser())
                        .build()
        );

        return token;
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        var claims = extractClaims(token);

        return claims.getSubject().equals(userDetails.getUsername())
                && !claims.getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] encodedKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
