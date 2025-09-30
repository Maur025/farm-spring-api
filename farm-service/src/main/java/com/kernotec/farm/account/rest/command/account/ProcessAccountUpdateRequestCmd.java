package com.kernotec.farm.account.rest.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountUpdateCmd;
import com.kernotec.farm.account.rest.dto.request.account.AccountUpdateRequest;
import com.kernotec.farm.util.LinkUtil;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessAccountUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessAccountUpdateRequestCmd.Request, Void>
{

    private final AccountUpdateCmd accountUpdateCmd;
    private final LinkUtil linkUtil;

    @Override
    protected Void run(Request request) {
        AccountUpdateRequest updateRequest = request.updateRequest();

        String identityUsername = null;

        if (updateRequest.getAccountLink() != null) {
            identityUsername = linkUtil.getIdentityFacebookOfLink(updateRequest.getAccountLink());
        }

        accountUpdateCmd.withRequest(AccountUpdateCmd.Request.builder()
                .accountId(request.accountId())
                .username(updateRequest.getUsername())
                .password(updateRequest.getPassword())
                .personId(updateRequest.getPersonId())
                .isEnabled(updateRequest.getIsEnabled())
                .accountLink(updateRequest.getAccountLink())
                .identityUsername(identityUsername)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID accountId, AccountUpdateRequest updateRequest) {

    }
}
