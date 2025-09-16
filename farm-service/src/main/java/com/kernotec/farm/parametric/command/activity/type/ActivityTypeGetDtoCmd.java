package com.kernotec.farm.parametric.command.activity.type;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.mapper.ActivityTypeDtoFlatMapper;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.service.ActivityTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityTypeGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ActivityTypeGetDtoCmd.Request, ActivityTypeDto>
{

    private final ActivityTypeDtoFlatMapper activityTypeDtoFlatMapper;
    private final ActivityTypeService activityTypeService;

    @Override
    protected ActivityTypeDto run(Request request) {
        ActivityType activityType = activityTypeService.findByIdThrow(request.getActivityTypeId());
        return activityTypeDtoFlatMapper.toDto(activityType);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID activityTypeId;
    }
}
