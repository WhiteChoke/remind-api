package com.whitechoke.remind.security.domain;

import com.whitechoke.remind.security.domain.dto.LoginRequestDto;
import com.whitechoke.remind.security.domain.dto.LoginResponseDto;
import com.whitechoke.remind.security.domain.dto.RegisterRequestDto;
import com.whitechoke.remind.security.domain.dto.RegisterResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody @Valid LoginRequestDto request,
            HttpServletResponse httpResponse
    ) {
        var response = authService.login(request, httpResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(
            @RequestBody @Valid RegisterRequestDto request,
            HttpServletResponse httpResponse
    ) {
        var response = authService.register(request, httpResponse);

        return ResponseEntity.ok(response);
    }
}
