package com.jaijobner.transport_new.repository;

import com.jaijobner.transport_new.entity.auth.PasswordResetToken;
import com.jaijobner.transport_new.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
