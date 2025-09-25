package com.kernotec.farm.activity.jpa.specification.criteria;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySpecificationCriteria {

    private Boolean includeOnlyUserActivities;
    private UUID userAuthId;
    private UUID socialNetworkId;
}
