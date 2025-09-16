package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDtoFlatMapper {

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "socialNetwork", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "observations", ignore = true)
    AccountDto toDto(Account account);

    List<AccountDto> toDto(List<Account> accountList);

    Set<AccountDto> toDto(Set<Account> accountSet);
}
