package com.kernotec.farm.command.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Group;
import com.kernotec.farm.jpa.enums.GroupActionEnum;
import com.kernotec.farm.jpa.service.GroupService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupCreateCmd extends
    AbstractTransactionalRequiredCommand<GroupCreateCmd.Request, UUID>
{

    private final GroupService groupService;

    @Override
    protected UUID run(Request request) {
        Group group = new Group();

        group.setName(request.getName());
        group.setAction(request.getAction());
        group.setRegionId(request.getRegionId());
        group.setActivityId(request.getActivityId());
        group.setActivityTypeId(request.getActivityTypeId());

        group = groupService.save(group);
        return group.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final GroupActionEnum action;
        @NotNull
        private final UUID requestStateId;
        @NotNull
        private final UUID regionId;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
