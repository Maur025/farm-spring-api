package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountResponseFlatMapper {

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "socialNetwork", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "observations", ignore = true)
    AccountResponse toResponse(Account account);

    AccountResponse toResponse(UUID id);

    List<AccountResponse> toResponse(List<Account> accountList);

    Set<AccountResponse> toResponse(Set<Account> accountSet);
}
