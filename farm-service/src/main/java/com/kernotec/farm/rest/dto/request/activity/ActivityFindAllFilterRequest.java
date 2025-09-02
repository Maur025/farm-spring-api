package com.kernotec.farm.rest.dto.request.activity;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityFindAllFilterRequest {

    private UUID accountId;
    private UUID socialNetworkId;
    private UUID deviceId;
}
