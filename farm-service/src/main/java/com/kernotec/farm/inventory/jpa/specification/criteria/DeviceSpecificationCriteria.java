package com.kernotec.farm.inventory.jpa.specification.criteria;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceSpecificationCriteria {

    private String orderBy;
    private boolean descending;

    private UUID farmId;
    private UUID socialNetworkId;
    private String keyword;
}
