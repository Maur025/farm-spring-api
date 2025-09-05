package com.kernotec.farm.activity.rest.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.rest.command.account.group.AccountGroupCreateInActionJoinCmd;
import com.kernotec.farm.activity.command.group.GroupCreateCmd;
import com.kernotec.farm.activity.command.group.membership.GroupMembershipCreateCmd;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipCreateRequest;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupMembershipCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<GroupMembershipCreateActivityRelationCmd.Request, Void>
{

    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;
    private final GroupCreateCmd groupCreateCmd;
    private final GroupMembershipCreateCmd groupMembershipCreateCmd;
    private final AccountGroupCreateInActionJoinCmd accountGroupCreateInActionJoinCmd;
    private final GroupMemberCreateActivityValidationCmd groupMemberCreateActivityValidationCmd;

    @Override
    protected Void run(Request request) {
        GroupMembershipCreateRequest groupMembershipRequest = request.getGroupMembershipRequest();

        if (groupMembershipRequest == null) {
            log.debug("No group membership request provided, skipping activity relation creation.");
            return null;
        }

        UUID requestStatePendingId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.PENDING)
                    .build())
            .execute();

        UUID requestStateNothingId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                    .build())
            .execute();

        UUID groupToRegisterId = groupMembershipRequest.getGroupId();

        if (groupMembershipRequest.getIsNewGroup() && groupToRegisterId == null) {
            groupToRegisterId = groupCreateCmd.withRequest(GroupCreateCmd.Request.builder()
                    .name(groupMembershipRequest.getGroupName())
                    .regionId(groupMembershipRequest.getRegionId())
                    .description("N/A")
                    .build())
                .execute();
        }

        groupMemberCreateActivityValidationCmd.withRequest(
                GroupMemberCreateActivityValidationCmd.Request.builder()
                    .accountId(request.getAccountId())
                    .groupId(groupToRegisterId)
                    .action(groupMembershipRequest.getAction())
                    .build())
            .execute();

        groupMembershipCreateCmd.withRequest(GroupMembershipCreateCmd.Request.builder()
                .action(groupMembershipRequest.getAction())
                .groupId(groupToRegisterId)
                .publishingContextId(groupMembershipRequest.getPublishingContextId())
                .requestStateId(groupMembershipRequest.getAction()
                    .equals(GroupActionEnum.JOIN_REQUEST) ? requestStatePendingId
                    : requestStateNothingId)
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        accountGroupCreateInActionJoinCmd.withRequest(
                AccountGroupCreateInActionJoinCmd.Request.builder()
                    .action(groupMembershipRequest.getAction())
                    .joinedAt(request.getActivityDate())
                    .accountId(request.getAccountId())
                    .groupId(groupToRegisterId)
                    .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final GroupMembershipCreateRequest groupMembershipRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
        private final UUID accountId;
        private final ZonedDateTime activityDate;
    }
}
