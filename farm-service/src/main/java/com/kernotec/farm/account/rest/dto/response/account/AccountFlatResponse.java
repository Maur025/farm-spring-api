package com.kernotec.farm.account.rest.dto.response.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountFlatResponse extends EntityResponse {

    private String username;
    private String password;
    private AccountTypeEnum type;
    private UUID personId;
    private UUID socialNetworkId;
}
