package com.kernotec.farm.activity.jpa.specification.criteria;

import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySpecificationCriteria {

    private Boolean includeOnlyUserActivities;
    private UUID userAuthId;
    private UUID socialNetworkId;
    private UUID deviceId;
    private UUID farmId;
    private UUID activityTypeId;
    private UUID accountId;
    private Set<UUID> accountIdList;
}
