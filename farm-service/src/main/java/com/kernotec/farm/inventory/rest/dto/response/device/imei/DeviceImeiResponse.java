package com.kernotec.farm.inventory.rest.dto.response.device.imei;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DeviceImeiResponse extends AuditEntityDto {

    private String imei;

    private UUID deviceId;
    private DeviceResponse device;
}
