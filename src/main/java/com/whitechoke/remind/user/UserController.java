package com.whitechoke.remind.user;

import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import com.whitechoke.remind.user.dto.UserCreateResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
}
