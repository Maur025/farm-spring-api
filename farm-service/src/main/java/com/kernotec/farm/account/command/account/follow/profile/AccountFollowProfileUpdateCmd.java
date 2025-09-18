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
public class AccountFollowProfileUpdateCmd extends
    AbstractTransactionalRequiredCommand<AccountFollowProfileUpdateCmd.Request, Void>
{

    private final AccountFollowProfileService accountFollowProfileService;

    @Override
    protected Void run(Request request) {
        AccountFollowProfile accountFollowProfile = accountFollowProfileService.findByIdThrow(
            request.getAccountFollowProfileId());

        if (request.getUnfollowedAt() != null) {
            accountFollowProfile.setUnfollowedAt(request.getUnfollowedAt());
        }

        if (request.getFollowState() != null) {
            accountFollowProfile.setFollowState(request.getFollowState());
        }

        accountFollowProfileService.save(accountFollowProfile);
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountFollowProfileId;

        private final ZonedDateTime unfollowedAt;
        private final AccountFollowProfileStateEnum followState;
    }
}
