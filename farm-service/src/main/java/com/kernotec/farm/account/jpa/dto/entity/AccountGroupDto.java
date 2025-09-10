package com.kernotec.farm.account.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.parametric.jpa.dto.entity.GroupStateDto;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountGroupDto extends AuditEntityDto {

    private ZonedDateTime joinedAt;
    private ZonedDateTime leftAt;

    private UUID accountId;
    private AccountDto account;

    private UUID groupId;
    private GroupDto group;

    private UUID groupStateId;
    private GroupStateDto groupState;
}
