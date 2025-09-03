package com.kernotec.farm.inventory.rest.dto.response.chip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ChipForActivityResponse extends EntityResponse {

    private String phoneNumber;
    private boolean isDeviceInside;
    private UUID registrationPersonId;
}
