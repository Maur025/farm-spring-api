package com.kernotec.farm.activity.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.service.GroupMembershipService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupMembershipUpdateCmd extends
    AbstractTransactionalRequiredCommand<GroupMembershipUpdateCmd.Request, Void>
{

    private final GroupMembershipService groupMembershipService;

    @Override
    protected Void run(Request request) {
        GroupMembership groupMembership = groupMembershipService.findByIdThrow(
            request.getGroupMembershipId());

        if (request.getRequestStateId() != null) {
            groupMembership.setRequestStateId(request.getRequestStateId());
        }

        groupMembershipService.save(groupMembership);
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID groupMembershipId;

        private final UUID requestStateId;
    }
}
