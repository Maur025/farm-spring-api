package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.inventory.rest.mapper.chip.ChipResponseFlatMapper;
import com.kernotec.farm.inventory.rest.mapper.device.DeviceResponseForActivityMapper;
import com.kernotec.farm.parametric.rest.mapper.social.network.SocialNetworkResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SocialNetworkResponseFlatMapper.class, DeviceResponseForActivityMapper.class,
    ChipResponseFlatMapper.class})
public interface AccountResponseForActivityMapper {

    @Mapping(target = "observations", ignore = true)
    AccountResponse toResponse(Account account);

    AccountResponse toResponse(UUID id);

    List<AccountResponse> toResponse(List<Account> accountList);

    Set<AccountResponse> toResponse(Set<Account> accountSet);
}
