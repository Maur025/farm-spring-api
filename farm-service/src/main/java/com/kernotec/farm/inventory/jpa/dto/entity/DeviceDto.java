package com.kernotec.farm.inventory.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceDto extends AuditEntityDto {

    private String name;
    private String deviceNumber;
    private String model;
    private String brand;
    private String serialNumber;

    private UUID farmId;
    private FarmDto farm;

    private Set<ChipDto> chips;
    private Set<AccountDto> accounts;
}
