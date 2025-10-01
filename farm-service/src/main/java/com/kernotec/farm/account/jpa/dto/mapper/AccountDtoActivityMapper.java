package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.inventory.jpa.dto.mapper.ChipDtoFlatMapper;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoActivityMapper;
import com.kernotec.farm.parametric.jpa.dto.mapper.SocialNetworkDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SocialNetworkDtoFlatMapper.class, DeviceDtoActivityMapper.class,
    ChipDtoFlatMapper.class})
public interface AccountDtoActivityMapper {

    @Mapping(target = "observations", ignore = true)
    AccountDto toDto(Account account);

    List<AccountDto> toDto(List<Account> accountList);

    Set<AccountDto> toDto(Set<Account> accountSet);
}
