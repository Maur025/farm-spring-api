package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface AccountResponseMapper {

    AccountResponse toResponse(UUID id);

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponse(List<Account> accountList);

    Set<AccountResponse> toResponse(Set<Account> accountSet);
}
