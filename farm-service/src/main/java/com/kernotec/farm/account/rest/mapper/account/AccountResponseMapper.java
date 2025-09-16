package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.inventory.rest.mapper.chip.ChipResponseFlatMapper;
import com.kernotec.farm.inventory.rest.mapper.device.DeviceResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.social.network.SocialNetworkResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {SocialNetworkResponseFlatMapper.class, DeviceResponseFlatMapper.class,
    ChipResponseFlatMapper.class})
public interface AccountResponseMapper {

    AccountResponse toResponse(UUID id);

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponse(List<Account> accountList);

    Set<AccountResponse> toResponse(Set<Account> accountSet);
}
