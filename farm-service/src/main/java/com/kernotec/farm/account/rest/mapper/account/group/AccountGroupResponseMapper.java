package com.kernotec.farm.account.rest.mapper.account.group;

import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.rest.dto.response.account.group.AccountGroupResponse;
import com.kernotec.farm.account.rest.mapper.account.AccountResponseFlatMapper;
import com.kernotec.farm.activity.rest.mapper.group.GroupResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountResponseFlatMapper.class, GroupResponseFlatMapper.class})
public interface AccountGroupResponseMapper {

    AccountGroupResponse toResponse(UUID id);

    AccountGroupResponse toResponse(AccountGroup accountGroup);

    List<AccountGroupResponse> toResponse(List<AccountGroup> accountGroupList);

    Set<AccountGroupResponse> toResponse(Set<AccountGroup> accountGroupSet);
}
