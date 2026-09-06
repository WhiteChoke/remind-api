package com.whitechoke.remind.user;

import com.whitechoke.remind.security.JwtUserDetails;
import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import com.whitechoke.remind.user.dto.UserCreateResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;

    // TODO: implement constraint exception handling, notification activation and mapping
    @CachePut(value = "users", key = "#result.id")
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto request) {
        var createdEntity = createEntity(request);
        return UserCreateResponseDto.builder()
                .id(createdEntity.getId())
                .username(createdEntity.getUsername())
                .build();
    }

    @Transactional
    public JwtUserDetails createUserWithJwtDetails(UserCreateRequestDto request) {
        var createdEntity = createEntity(request);
        return new JwtUserDetails(createdEntity);
    }

    @Transactional
    private UserEntity createEntity(UserCreateRequestDto request) {
        var encodedPassword = passwordEncoder.encode(request.password());

        var entity = userRepository.save(
                UserEntity.builder()
                        .username(request.username())
                        .email(request.email())
                        .password(encodedPassword)
                        .registeredAt(Instant.now())
                        .timezone(request.timezone())
                        .build()
        );

        log.info("Created user with id={}", entity.getId());

        return entity;
    }

    @CacheEvict(value = "users", key = "#result")
    @Transactional
    public Long deleteUser() {
        var id = currentUserProvider.getId();
        userRepository.deleteById(id);

        return id;
    }
}
