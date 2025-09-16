package com.kernotec.farm.activity.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.activity.jpa.service.GroupMembershipService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupMembershipCreateCmd extends
    AbstractTransactionalRequiredCommand<GroupMembershipCreateCmd.Request, UUID>
{

    private final GroupMembershipService groupMembershipService;

    @Override
    protected UUID run(Request request) {
        GroupMembership groupMembership = new GroupMembership();

        groupMembership.setAction(request.getAction());
        groupMembership.setGroupId(request.getGroupId());
        groupMembership.setRequestStateId(request.getRequestStateId());
        groupMembership.setActivityId(request.getActivityId());
        groupMembership.setActivityTypeId(request.getActivityTypeId());

        groupMembership = groupMembershipService.save(groupMembership);
        return groupMembership.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final GroupActionEnum action;
        @NotNull
        private final UUID groupId;
        @NotNull
        private final UUID requestStateId;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
