package com.kernotec.farm.activity.jpa.specification.criteria;

import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySpecificationCriteria {

    private String orderBy;
    private boolean isDescending;

    private Boolean includeOnlyUserActivities;
    private UUID userAuthId;
    private UUID socialNetworkId;
    private UUID deviceId;
    private UUID farmId;
    private UUID activityTypeId;
    private UUID accountId;
    private Set<UUID> accountIdList;
    private ZonedDateTime SimpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;
    private String zoneId;
    private String keyword;
    private TemporyDateForRequestEnum temporaryDateEnum;
}
