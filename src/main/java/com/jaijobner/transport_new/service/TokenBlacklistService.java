package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.config.JwtUtils;
import com.jaijobner.transport_new.entity.BlacklistedToken;
import com.jaijobner.transport_new.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HexFormat;

@Service
public class TokenBlacklistService {

    @Autowired
    private BlacklistedTokenRepository tokenRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public void addToBlacklist(String token) {
        String tokenHash = hashToken(token);
        LocalDateTime expiryDate = extractExpiryDate(token);
        BlacklistedToken blacklistedToken = new BlacklistedToken(tokenHash, expiryDate);
        tokenRepository.save(blacklistedToken);
    }

    // Check if token is blacklisted
    public boolean isBlacklisted(String token) {
        String tokenHash = hashToken(token);
        return tokenRepository.findByTokenHash(tokenHash).isPresent();
    }

    // Scheduled cleanup – runs daily at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

    // ---------- private helpers ----------

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes());
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private LocalDateTime extractExpiryDate(String token) {
        try {
            Date expiration = jwtUtils.getExpirationDateFromToken(token);
            return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
        } catch (ExpiredJwtException e) {
            // Token already expired – we still need to store it for blacklisting
            // Use the expiration from the exception
            Date expiration = e.getClaims().getExpiration();
            return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
        } catch (Exception e) {
            // If we cannot extract expiry (e.g., malformed token), set a default: now + 1 hour
            return LocalDateTime.now().plusHours(1);
        }
    }
}
