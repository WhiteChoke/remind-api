package com.whitechoke.remind.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private Integer streak = 0;
    private Instant lastReviewAt;
    private Instant registeredAt;
    private String timezone;
}
