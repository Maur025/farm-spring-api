package com.kernotec.farm.inventory.rest.dto.response.device.connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DeviceConnectionResponse extends EntityResponse {

    private String macAddress;
    private String ipAddress;
    private String ipGateway;
    private String networkName;

    private UUID deviceId;
    private DeviceResponse device;
}
