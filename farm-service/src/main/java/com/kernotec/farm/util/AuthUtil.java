package com.kernotec.farm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUtil {

    public String getAuthNameFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationJwt) {
            return authenticationJwt.getToken()
                .getClaim("name");
        }

        return "Unknown";
    }
}
