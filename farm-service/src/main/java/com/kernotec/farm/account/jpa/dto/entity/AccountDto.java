package com.kernotec.farm.account.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto extends AuditEntityDto {

    private String username;
    private String password;
    private AccountTypeEnum type;
    private Boolean isEnabled;

    private UUID personId;
    private PersonDto person;

    private UUID socialNetworkId;
    private SocialNetworkDto socialNetwork;

    private Set<DeviceDto> devices;
    private Set<ChipDto> chips;
    private Set<ObservationDto> observations;
}
