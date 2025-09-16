package com.kernotec.farm.account.rest.command.friend;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.friend.FriendGetDtoCmd;
import com.kernotec.farm.account.command.friend.FriendUpdateCmd;
import com.kernotec.farm.account.jpa.dto.entity.FriendDto;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.service.FriendService;
import com.kernotec.farm.account.rest.dto.request.friend.FriendBreakRelationshipRequest;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.exception.FriendException;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.parametric.command.friend.state.FriendStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.ActivityTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendBreakRelationshipCmd extends
    AbstractTransactionalRequiredCommand<FriendBreakRelationshipCmd.Request, Void>
{

    private final FriendGetDtoCmd friendGetDtoCmd;
    private final FriendService friendService;
    private final FriendStateGetIdByCodeCmd friendStateGetIdByCodeCmd;
    private final FriendUpdateCmd friendUpdateCmd;
    private final ActivityCreateCmd activityCreateCmd;
    private final ActivityTypeService activityTypeService;
    private final ConnectionCreateCmd connectionCreateCmd;
    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;

    @Override
    protected Void run(Request request) {
        FriendDto friendDto = friendGetDtoCmd.withRequest(FriendGetDtoCmd.Request.builder()
                .friendId(request.getFriendId())
                .build())
            .execute();

        if (FriendStateCodeEnum.ENDED.toString()
            .equals(friendDto.getFriendState()
                .getCode()))

        {
            throw new FriendException(
                "friendship.already.ended", "'" + friendDto.getAccount()
                .getUsername() + "'", HttpStatus.CONFLICT.value()
            );
        }

        UUID friendStateActiveId = friendStateGetIdByCodeCmd.withRequest(
                FriendStateGetIdByCodeCmd.Request.builder()
                    .code(FriendStateCodeEnum.ACTIVE)
                    .build())
            .execute();

        UUID friendStateEndedId = friendStateGetIdByCodeCmd.withRequest(
                FriendStateGetIdByCodeCmd.Request.builder()
                    .code(FriendStateCodeEnum.ENDED)
                    .build())
            .execute();

        Friend friendSecondary = friendService.findByAccountIdAndFriendAccountIdAndFriendStateIdThrow(
            friendDto.getFriendAccountId(), friendDto.getAccountId(), friendStateActiveId);

        FriendBreakRelationshipRequest breakRequest = request.getBreakRequest();

        friendUpdateCmd.withRequest(FriendUpdateCmd.Request.builder()
                .friendId(request.getFriendId())
                .endedAt(breakRequest.getBreakDate())
                .friendStateId(friendStateEndedId)
                .build())
            .execute();

        friendUpdateCmd.withRequest(FriendUpdateCmd.Request.builder()
                .friendId(friendSecondary.getId())
                .endedAt(breakRequest.getBreakDate())
                .friendStateId(friendStateEndedId)
                .build())
            .execute();

        ActivityType activityType = activityTypeService.findByCodeThrow(
            ActivityTypeCodeEnum.AMISTAD);
        UUID requestStateId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                    .build())
            .execute();

        UUID activityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link("N/A")
                .activityDate(breakRequest.getBreakDate())
                .accountId(friendDto.getAccountId())
                .activityTypeId(activityType.getId())
                .build())
            .execute();

        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(friendDto.getFriendAccountId())
                .action(ConnectionActionEnum.BREAK_CONNECTION)
                .type(ConnectionTypeEnum.fromValue(friendDto.getAccount()
                    .getType()))
                .requestStateId(requestStateId)
                .activityId(activityId)
                .activityTypeId(activityType.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID friendId;
        @NotNull
        private final FriendBreakRelationshipRequest breakRequest;
    }
}
