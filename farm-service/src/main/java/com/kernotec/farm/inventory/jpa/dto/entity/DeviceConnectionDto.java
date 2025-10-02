package com.kernotec.farm.inventory.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceConnectionDto extends AuditEntityDto {

    private String macAddress;
    private String ipAddress;
    private String ipGateway;
    private String networkName;

    private UUID deviceId;
    private DeviceDto device;
}
