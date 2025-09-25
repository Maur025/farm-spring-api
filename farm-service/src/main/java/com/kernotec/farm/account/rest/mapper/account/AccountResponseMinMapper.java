package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountResponseMinMapper {

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "socialNetworkId", ignore = true)
    @Mapping(target = "socialNetwork", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "accountExtension", ignore = true)
    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponse(List<Account> accountList);

    Set<AccountResponse> toResponse(Set<Account> accountSet);
}
