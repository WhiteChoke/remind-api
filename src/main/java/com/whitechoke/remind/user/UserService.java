package com.whitechoke.remind.user;

import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import com.whitechoke.remind.user.dto.UserCreateResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // TODO: implement constraint exception handling, notification activation and mapping
    @CachePut(value = "users::", key = "#id")
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto request) {
        var createdEntity = userRepository.save(
                UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .registeredAt(Instant.now())
                .timezone(request.timezone())
                .build()
        );

        return UserCreateResponseDto.builder()
                .id(createdEntity.getId())
                .username(createdEntity.getUsername())
                .build();
    }
}
