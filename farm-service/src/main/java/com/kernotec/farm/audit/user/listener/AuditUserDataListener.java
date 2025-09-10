package com.kernotec.farm.audit.user.listener;

import com.kernotec.farm.audit.user.DataBaseAuditEntity;
import com.kernotec.farm.audit.user.json.AuthUserData;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuditUserDataListener {

    @PrePersist
    public void onPrePersist(DataBaseAuditEntity entity) {
        AuthUserData authUserData = getAuthUserData();

        if (authUserData != null) {
            entity.setCreatedByData(authUserData);
            entity.setUpdatedByData(authUserData);
        }
    }

    @PreUpdate
    public void onPreUpdate(DataBaseAuditEntity entity) {
        AuthUserData authUserData = getAuthUserData();

        if (authUserData != null) {
            entity.setUpdatedByData(authUserData);
        }
    }

    private AuthUserData getAuthUserData() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return getUserData(jwtAuthenticationToken);
        }

        return null;
    }

    private AuthUserData getUserData(JwtAuthenticationToken jwtAuthenticationToken) {
        Map<String, List<String>> realmAccess = jwtAuthenticationToken.getToken()
            .getClaim("realm_access");

        return AuthUserData.builder()
            .id(jwtAuthenticationToken.getToken()
                .getClaim("sub"))
            .username(jwtAuthenticationToken.getToken()
                .getClaim("preferred_username"))
            .name(jwtAuthenticationToken.getToken()
                .getClaim("name"))
            .roles(realmAccess.get("roles"))
            .authTime(jwtAuthenticationToken.getToken()
                .getClaim("auth_time"))
            .build();
    }
}
