package com.kernotec.farm.inventory.rest.dto.response.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.inventory.rest.dto.response.chip.ChipResponse;
import com.kernotec.farm.inventory.rest.dto.response.device.imei.DeviceImeiResponse;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmResponse;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DeviceResponse extends EntityResponse {

    private String name;
    private String deviceNumber;
    private String model;
    private String brand;
    private String serialNumber;

    private UUID farmId;
    private FarmResponse farm;

    private Set<ChipResponse> chips;
    private Set<AccountResponse> accounts;
    private Set<DeviceImeiResponse> deviceImeis;
}
