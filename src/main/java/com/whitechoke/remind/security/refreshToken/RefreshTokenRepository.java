package com.whitechoke.remind.security.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    @Modifying
    @Query("delete from RefreshTokenEntity e where user.id = :userId")
    void deleteByUserId(@Param("userId") Long id);
    Optional<RefreshTokenEntity> findByToken(String token);
}
