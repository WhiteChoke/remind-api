package com.whitechoke.remind.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Users",
        indexes = {
                @Index(name = "idx_users_username", columnList = "username", unique = true),
                @Index(name = "idx_users_email", columnList = "email", unique = true)
        }
)
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "username",unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "streak")
    private Integer streak = 0;
    @Column(name = "last_review_at")
    private Instant lastReviewAt;
    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;
    @Column(name = "timezone", nullable = false)
    private String timezone;
}
