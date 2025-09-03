package com.kernotec.farm.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.rest.dto.response.account.AccountFlatResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface AccountFlatResponseMapper {

    AccountFlatResponse toResponse(UUID id);

    AccountFlatResponse toResponse(Account account);

    List<AccountFlatResponse> toResponse(List<Account> accountList);

    Set<AccountFlatResponse> toResponse(Set<Account> accountSet);
}
