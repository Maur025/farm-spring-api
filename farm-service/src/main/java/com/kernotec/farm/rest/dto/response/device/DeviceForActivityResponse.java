package com.kernotec.farm.rest.dto.response.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.rest.dto.response.farm.FarmResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DeviceForActivityResponse extends EntityResponse {

    private String name;
    private String deviceNumber;
    private String model;
    private String brand;
    private String serialNumber;

    private UUID farmId;
    private FarmResponse farm;
}
