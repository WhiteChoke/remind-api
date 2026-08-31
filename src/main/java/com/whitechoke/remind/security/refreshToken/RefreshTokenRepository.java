package com.whitechoke.remind.security.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    void deleteByUserId(Long id);
}
