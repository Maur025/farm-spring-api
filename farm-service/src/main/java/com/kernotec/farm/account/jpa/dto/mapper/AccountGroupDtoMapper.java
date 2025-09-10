package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.AccountGroupDto;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.activity.jpa.dto.mapper.GroupDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountDtoFlatMapper.class, GroupDtoFlatMapper.class})
public interface AccountGroupDtoMapper {

    AccountGroupDto toDto(AccountGroup accountGroup);

    List<AccountGroupDto> toDto(List<AccountGroup> accountGroupList);

    Set<AccountGroupDto> toDto(Set<AccountGroup> accountGroupSet);
}
