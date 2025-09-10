package com.kernotec.farm.audit.user.dto.response;

import com.kernotec.core.rest.dto.response.data.AuditEntityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DataAuditEntityResponse extends AuditEntityResponse {

    private AuthUserDataResponse createdByData;
    private AuthUserDataResponse updatedByData;
}
