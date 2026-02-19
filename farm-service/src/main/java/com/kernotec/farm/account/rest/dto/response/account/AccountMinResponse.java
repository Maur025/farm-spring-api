package com.kernotec.farm.account.rest.dto.response.account;

import java.util.UUID;

public interface AccountMinResponse {

    UUID getId();

    String getUsername();

    String getAccountLink();
}
