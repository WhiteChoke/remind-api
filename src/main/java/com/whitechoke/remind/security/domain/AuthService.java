package com.whitechoke.remind.security.domain;

import com.whitechoke.remind.security.JwtUserDetails;
import com.whitechoke.remind.security.JwtUtils;
import com.whitechoke.remind.security.domain.dto.LoginRequestDto;
import com.whitechoke.remind.security.domain.dto.LoginResponseDto;
import com.whitechoke.remind.security.domain.dto.RegisterRequestDto;
import com.whitechoke.remind.security.domain.dto.RegisterResponseDto;
import com.whitechoke.remind.user.UserService;
import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(
            LoginRequestDto request,
            HttpServletResponse httpResponse
    ) {
        var userDetails = (JwtUserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var accessToken = jwtUtils.generateAccessToken(userDetails);

        setRefreshTokenCookie(httpResponse, userDetails);

        return LoginResponseDto.builder()
                .username(userDetails.getUsername())
                .id(userDetails.getId())
                .accessToken(accessToken)
                .build();
    }

    public RegisterResponseDto register(
            RegisterRequestDto request,
            HttpServletResponse httpResponse
    ) {
        var createdUserDetails = userService.createUserWithJwtDetails(
                UserCreateRequestDto.builder()
                        .username(request.username())
                        .password(request.password())
                        .email(request.email())
                        .timezone(request.timezone())
                        .build()
        );

        setRefreshTokenCookie(httpResponse, createdUserDetails);

        var accessToken = jwtUtils.generateAccessToken(createdUserDetails);

        return RegisterResponseDto.builder()
                .id(createdUserDetails.getId())
                .username(createdUserDetails.getUsername())
                .accessToken(accessToken)
                .build();
    }


    private void setRefreshTokenCookie(
            HttpServletResponse httpResponse,
            JwtUserDetails userDetails
    ) {
        var token = jwtUtils.generateRefreshToken(userDetails);

        var cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .maxAge(jwtUtils.getRefreshExpiryMs()/1000)
                .path("/")
                .sameSite("Lax")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
