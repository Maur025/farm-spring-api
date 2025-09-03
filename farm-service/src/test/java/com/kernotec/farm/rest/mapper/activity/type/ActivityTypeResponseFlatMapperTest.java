package com.kernotec.farm.rest.mapper.activity.type;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kernotec.core.test.unit.test.util.AbstractMapperTest;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.rest.dto.response.activity.type.ActivityTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.Mapper;
import org.mockito.InjectMocks;

@Mapper
class ActivityTypeResponseFlatMapperTest extends
    AbstractMapperTest<ActivityType, ActivityTypeResponse>
{

    @InjectMocks
    private ActivityTypeResponseFlatMapperImpl activityTypeResponseFlatMapper;

    private ActivityType activityType;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        activityType = new ActivityType();

        activityType.setId(UUID.randomUUID());
        activityType.setName("test name");
        activityType.setCode("test code");

        activityType.setSocialNetworks(Set.of());

        setInputObject(activityType);
    }

    @Override
    protected ActivityTypeResponse executeMapper(ActivityType activityType) {
        return activityTypeResponseFlatMapper.toResponse(activityType);
    }

    @Override
    protected List<ActivityTypeResponse> executeMapper(List<ActivityType> activityTypeList) {
        return activityTypeResponseFlatMapper.toResponse(activityTypeList);
    }

    @Override
    protected Set<ActivityTypeResponse> executeMapper(Set<ActivityType> activityTypeSet) {
        return activityTypeResponseFlatMapper.toResponse(activityTypeSet);
    }

    @Override
    protected void assertsForResult(ActivityTypeResponse activityTypeResponse) {
        assertAll(
            "ActivityTypeResponse asserts", () -> assertNotNull(activityTypeResponse.getId()),
            () -> assertEquals(activityType.getId(), activityTypeResponse.getId()),

            () -> assertNotNull(activityTypeResponse.getName()),
            () -> assertEquals(activityType.getName(), activityTypeResponse.getName()),

            () -> assertNotNull(activityTypeResponse.getCode()),
            () -> assertEquals(activityType.getCode(), activityTypeResponse.getCode()),

            () -> assertNull(activityTypeResponse.getSocialNetworks())
        );
    }
}