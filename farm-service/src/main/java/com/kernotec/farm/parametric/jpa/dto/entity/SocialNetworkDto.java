package com.kernotec.farm.parametric.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialNetworkDto extends AuditEntityDto {

    private String name;
    private String code;
    private String icon;
    private String color;

    private Set<AccountDto> accounts;
}
