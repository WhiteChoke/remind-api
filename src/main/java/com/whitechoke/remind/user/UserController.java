package com.whitechoke.remind.user;

import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import com.whitechoke.remind.user.dto.UserCreateResponseDto;
import com.whitechoke.remind.user.dto.UserGetResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<Long> deleteUser() {
        var response = userService.deleteUser();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserCreateResponseDto> createUser(
            @Valid @RequestBody UserCreateRequestDto request
    ) {
        var response = userService.createUser(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserGetResponseDto> getCurrentUser() {
        var response = userService.getCurrentUser();

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponseDto> getUserById(@PathVariable Long id) {
        var response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }
}
