package com.kernotec.farm.account.rest.command.account.extension;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.extension.AccountExtensionCreateCmd;
import com.kernotec.farm.account.command.account.extension.log.AccountExtensionLogCreateCmd;
import com.kernotec.farm.account.jpa.service.AccountExtensionService;
import com.kernotec.farm.account.rest.dto.request.account.AccountExtensionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessAccountExtensionCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessAccountExtensionCreateRequestCmd.Request, UUID>
{

    private final AccountExtensionService accountExtensionService;
    private final AccountExtensionCreateCmd accountExtensionCreateCmd;
    private final AccountExtensionLogCreateCmd accountExtensionLogCreateCmd;

    @Override
    protected UUID run(Request request) {
        AccountExtensionRequest accountExtensionRequest = request.accountExtensionRequest;

        accountExtensionService.deleteAllByAccountId(request.accountId);

        UUID accountExtensionId = accountExtensionCreateCmd.withRequest(
                AccountExtensionCreateCmd.Request.builder()
                    .accountId(request.accountId)
                    .referenceEmail(accountExtensionRequest.getReferenceEmail())
                    .build())
            .execute();

        accountExtensionLogCreateCmd.withRequest(AccountExtensionLogCreateCmd.Request.builder()
                .referenceEmailValue(accountExtensionRequest.getReferenceEmail())
                .accountId(request.accountId)
                .build())
            .execute();

        return accountExtensionId;
    }

    @Builder
    public record Request(@NotNull UUID accountId,
                          @NotNull @Valid AccountExtensionRequest accountExtensionRequest)
    {

    }
}
