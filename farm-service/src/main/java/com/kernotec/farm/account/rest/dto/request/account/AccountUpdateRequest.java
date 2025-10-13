package com.kernotec.farm.account.rest.dto.request.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AccountUpdateRequest extends BaseRequest {

    private String username;
    private String password;
    private UUID personId;
    private Boolean isEnabled;
    private String accountLink;
}
