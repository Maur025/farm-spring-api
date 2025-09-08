package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountResponseToDeviceMapper {

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "socialNetwork", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "observations", ignore = true)
    AccountResponse toResponse(Account account);

    default List<AccountResponse> toResponse(List<Account> accountList,
        @Context UUID socialNetworkId, @Context String keyword)
    {
        Stream<Account> accountStream = accountList.stream();

        accountStream = AccountResponseFilter.getStreamWithFilters(
            accountStream, socialNetworkId, keyword);

        return accountStream.map(this::toResponse)
            .toList();
    }

    default Set<AccountResponse> toResponse(Set<Account> accountSet, @Context UUID socialNetworkId,
        @Context String keyword)
    {
        Stream<Account> accountStream = accountSet.stream();

        accountStream = AccountResponseFilter.getStreamWithFilters(
            accountStream, socialNetworkId, keyword);

        return accountStream.map(this::toResponse)
            .collect(Collectors.toSet());
    }
}
