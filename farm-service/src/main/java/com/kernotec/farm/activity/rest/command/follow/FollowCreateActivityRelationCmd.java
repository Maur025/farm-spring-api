package com.kernotec.farm.activity.rest.command.follow;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.follow.profile.AccountFollowProfileCreateCmd;
import com.kernotec.farm.account.command.account.follow.profile.AccountFollowProfileUpdateCmd;
import com.kernotec.farm.account.exception.AccountFollowProfileException;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.account.jpa.service.AccountFollowProfileService;
import com.kernotec.farm.activity.command.follow.FollowCreateCmd;
import com.kernotec.farm.activity.rest.command.profile.ProfileGetOrCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<FollowCreateActivityRelationCmd.Request, Void>
{

    private final FollowCreateCmd followCreateCmd;
    private final ProfileGetOrCreateCmd profileGetOrCreateCmd;
    private final AccountFollowProfileCreateCmd accountFollowProfileCreateCmd;
    private final AccountFollowProfileService accountFollowProfileService;
    private final AccountFollowProfileUpdateCmd accountFollowProfileUpdateCmd;

    @Override
    protected Void run(Request request) {
        FollowCreateRequest followRequest = request.getFollowRequest();

        if (followRequest == null || followRequest.getName() == null || followRequest.getName()
            .isBlank())
        {
            log.debug("FollowCreateRequest is null or name is blank");

            return null;
        }

        UUID profileId = profileGetOrCreateCmd.withRequest(ProfileGetOrCreateCmd.Request.builder()
                .name(followRequest.getName())
                .publishingContextId(followRequest.getPublishingContextId())
                .regionId(followRequest.getRegionId())
                .build())
            .execute();

        followCreateCmd.withRequest(FollowCreateCmd.Request.builder()
                .name(followRequest.getName())
                .isFollowing(followRequest.getIsFollowing() != null && followRequest.getIsFollowing())
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .publishingContextId(followRequest.getPublishingContextId())
                .regionId(followRequest.getRegionId())
                .profileId(profileId)
                .build())
            .execute();

        Optional<AccountFollowProfile> accountFollowProfile = accountFollowProfileService.findByAccountIdAndProfileIdAndFollowState(
            request.getAccountId(), profileId, AccountFollowProfileStateEnum.FOLLOWING);

        if (followRequest.getIsFollowing()) {
            if (accountFollowProfile.isPresent()) {
                throw new AccountFollowProfileException(
                    "already.follow", "'" + followRequest.getName() + "'",
                    HttpStatus.BAD_REQUEST.value()
                );
            }

            accountFollowProfileCreateCmd.withRequest(
                    AccountFollowProfileCreateCmd.Request.builder()
                        .followedAt(request.getActivityDate())
                        .followState(AccountFollowProfileStateEnum.FOLLOWING)
                        .accountId(request.getAccountId())
                        .profileId(profileId)
                        .build())
                .execute();
        } else {
            if (accountFollowProfile.isEmpty()) {
                throw new AccountFollowProfileException(
                    "not.follow", "'" + followRequest.getName() + "'",
                    HttpStatus.BAD_REQUEST.value()
                );
            }

            accountFollowProfileUpdateCmd.withRequest(
                    AccountFollowProfileUpdateCmd.Request.builder()
                        .accountFollowProfileId(accountFollowProfile.get()
                            .getId())
                        .unfollowedAt(request.getActivityDate())
                        .followState(AccountFollowProfileStateEnum.UNFOLLOWED)
                        .build())
                .execute();
        }

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final FollowCreateRequest followRequest;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
        @NotNull
        private final ZonedDateTime activityDate;
        @NotNull
        private final UUID accountId;
    }
}
