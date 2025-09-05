package com.kernotec.farm.account.rest.mapper.account.group;

import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.rest.dto.response.account.group.AccountGroupResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface AccountGroupResponseMapper {

    AccountGroupResponse toResponse(UUID id);

    AccountGroupResponse toResponse(AccountGroup accountGroup);

    List<AccountGroupResponse> toResponse(List<AccountGroup> accountGroupList);

    Set<AccountGroupResponse> toResponse(Set<AccountGroup> accountGroupSet);
}
