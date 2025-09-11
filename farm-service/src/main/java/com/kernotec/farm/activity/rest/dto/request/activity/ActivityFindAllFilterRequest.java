package com.kernotec.farm.activity.rest.dto.request.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ActivityFindAllFilterRequest extends BaseRequest {

    private Set<UUID> accountIds;
    private UUID socialNetworkId;
    private UUID deviceId;
}
