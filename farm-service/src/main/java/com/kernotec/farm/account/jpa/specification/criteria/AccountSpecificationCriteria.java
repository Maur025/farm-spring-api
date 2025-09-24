package com.kernotec.farm.account.jpa.specification.criteria;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSpecificationCriteria {

    private UUID socialNetworkId;
    private UUID ignoreAccountId;
    private String usernameSearch;
    private String keyword;
}
