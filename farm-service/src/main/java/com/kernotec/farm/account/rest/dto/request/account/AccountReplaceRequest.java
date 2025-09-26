package com.kernotec.farm.account.rest.dto.request.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AccountReplaceRequest extends BaseRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;

    @NotNull
    private Boolean replacePerson;
    private String fakeName;
    private String fakeLastName;
}
