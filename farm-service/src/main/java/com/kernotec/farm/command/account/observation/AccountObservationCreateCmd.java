package com.kernotec.farm.command.account.observation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.AccountObservation;
import com.kernotec.farm.jpa.service.AccountObservationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountObservationCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountObservationCreateCmd.Request, UUID>
{

    private final AccountObservationService accountObservationService;

    @Override
    protected UUID run(Request request) {
        AccountObservation accountObservation = new AccountObservation();

        accountObservation.setAccountId(request.getAccountId());
        accountObservation.setObservationId(request.getObservationId());

        accountObservation = accountObservationService.save(accountObservation);
        return accountObservation.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountId;
        @NotNull
        private final UUID observationId;
    }
}
