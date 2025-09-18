package com.kernotec.farm.account.command.account.follow.profile;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.account.jpa.service.AccountFollowProfileService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountFollowProfileCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountFollowProfileCreateCmd.Request, UUID>
{

    private final AccountFollowProfileService accountFollowProfileService;

    @Override
    protected UUID run(Request request) {
        AccountFollowProfile accountFollowProfile = new AccountFollowProfile();

        accountFollowProfile.setFollowedAt(request.getFollowedAt());
        accountFollowProfile.setUnfollowedAt(request.getUnfollowedAt());
        accountFollowProfile.setFollowState(request.getFollowState());
        accountFollowProfile.setAccountId(request.getAccountId());
        accountFollowProfile.setProfileId(request.getProfileId());

        accountFollowProfile = accountFollowProfileService.save(accountFollowProfile);
        return accountFollowProfile.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ZonedDateTime followedAt;
        private final ZonedDateTime unfollowedAt;
        @NotNull
        private final AccountFollowProfileStateEnum followState;
        @NotNull
        private final UUID accountId;
        private final UUID profileId;
    }
}
