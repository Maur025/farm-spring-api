package com.kernotec.farm.jpa.dto.entity.device;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.jpa.dto.entity.farm.FarmDto;
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
