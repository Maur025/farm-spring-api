package com.kernotec.farm.inventory.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
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
}
