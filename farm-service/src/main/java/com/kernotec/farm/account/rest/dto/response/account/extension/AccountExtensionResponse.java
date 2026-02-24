package com.kernotec.farm.account.rest.dto.response.account.extension;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountExtensionResponse extends EntityResponse {

    private String referenceEmail;
    private UUID accountId;
}
