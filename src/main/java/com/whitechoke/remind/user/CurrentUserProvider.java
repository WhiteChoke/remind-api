package com.whitechoke.remind.user;

import com.whitechoke.remind.security.JwtUserDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public Long getId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JwtUserDetails details)) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user");
        }
        return details.getId();
    }
}
