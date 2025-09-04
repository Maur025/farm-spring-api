package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.inventory.jpa.dto.mapper.ChipDtoFlatMapper;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoFlatMapper;
import com.kernotec.farm.parametric.jpa.dto.mapper.SocialNetworkDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(
    uses = {SocialNetworkDtoFlatMapper.class, DeviceDtoFlatMapper.class, ChipDtoFlatMapper.class})
public interface AccountDtoMapper {

    AccountDto toDto(Account account);

    List<AccountDto> toDto(List<Account> accountList);

    Set<AccountDto> toDto(Set<Account> accountSet);
}
