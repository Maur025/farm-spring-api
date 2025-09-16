package com.kernotec.farm.account.rest.dto.response.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.rest.dto.response.observation.ObservationResponse;
import com.kernotec.farm.account.rest.dto.response.person.PersonResponse;
import com.kernotec.farm.inventory.rest.dto.response.chip.ChipResponse;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkResponse;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountResponse extends EntityResponse {

    private String username;
    private String password;

    private AccountTypeEnum type;

    private UUID personId;
    private PersonResponse person;

    private UUID socialNetworkId;
    private SocialNetworkResponse socialNetwork;

    private Set<DeviceResponse> devices;
    private Set<ChipResponse> chips;

    private Set<ObservationResponse> observations;
}
