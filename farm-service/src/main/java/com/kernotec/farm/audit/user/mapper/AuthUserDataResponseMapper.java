package com.kernotec.farm.audit.user.mapper;

import com.kernotec.farm.audit.user.dto.response.AuthUserDataResponse;
import com.kernotec.farm.audit.user.json.AuditUserData;
import com.kernotec.farm.audit.user.json.AuthUserData;
import org.mapstruct.Mapper;

@Mapper
public interface AuthUserDataResponseMapper {

    default AuthUserDataResponse toResponse(AuditUserData auditUserData) {
        if (auditUserData == null) {
            return null;
        }

        if (auditUserData instanceof AuthUserData authUserData) {
            AuthUserDataResponse response = new AuthUserDataResponse();

            response.setId(authUserData.getId());
            response.setUsername(authUserData.getUsername());
            response.setName(authUserData.getName());
            response.setRoles(authUserData.getRoles());
            response.setAuthTime(authUserData.getAuthTime());

            return response;
        }

        return null;
    }
}
