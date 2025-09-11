package com.kernotec.farm.account.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.FriendStateDto;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDto extends AuditEntityDto {

    private ZonedDateTime acceptedAt;
    private ZonedDateTime endedAt;

    private UUID accountId;
    private AccountDto account;

    private UUID friendAccountId;
    private AccountDto friendAccount;

    private UUID friendStateId;
    private FriendStateDto friendState;
}
