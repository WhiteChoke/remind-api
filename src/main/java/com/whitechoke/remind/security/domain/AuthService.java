package com.whitechoke.remind.security.domain;

import com.whitechoke.remind.security.JwtUserDetails;
import com.whitechoke.remind.security.JwtUtils;
import com.whitechoke.remind.security.domain.dto.AccessTokenGetResponseDto;
import com.whitechoke.remind.security.domain.dto.LoginRequestDto;
import com.whitechoke.remind.security.domain.dto.LoginResponseDto;
import com.whitechoke.remind.security.domain.dto.RegisterRequestDto;
import com.whitechoke.remind.security.domain.dto.RegisterResponseDto;
import com.whitechoke.remind.security.refreshToken.RefreshTokenRepository;
import com.whitechoke.remind.user.UserService;
import com.whitechoke.remind.user.dto.UserCreateRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public LoginResponseDto login(
            LoginRequestDto request,
            HttpServletResponse httpResponse
    ) {
        var userDetails = (JwtUserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        ).getPrincipal();

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

    public AccessTokenGetResponseDto getAccessToken(
            HttpServletRequest request
    ) {
        var refreshToken = getCookie(request, REFRESH_TOKEN_COOKIE);

        var refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthorizationDeniedException("Refresh token not found"));

        if (refreshTokenEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthorizationDeniedException("Refresh token expired");
        }

        var accessToken = jwtUtils.generateAccessToken(
                new JwtUserDetails(refreshTokenEntity.getUser())
        );

        return AccessTokenGetResponseDto.builder()
                .id(refreshTokenEntity.getUser().getId())
                .username(refreshTokenEntity.getUser().getUsername())
                .token(accessToken)
                .build();
    }

    private String getCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void setRefreshTokenCookie(
            HttpServletResponse httpResponse,
            JwtUserDetails userDetails
    ) {
        var token = jwtUtils.generateRefreshToken(userDetails);

        var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .maxAge(jwtUtils.getRefreshExpiryMs()/1000)
                .path("/")
                .sameSite("Lax")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
