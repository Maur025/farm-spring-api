package com.kernotec.farm.account.command.account.observation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountObservation;
import com.kernotec.farm.account.jpa.service.AccountObservationService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountObservationCreateManyCmd extends
    AbstractTransactionalRequiredCommand<AccountObservationCreateManyCmd.Request, List<AccountObservation>>
{

    private final AccountObservationService accountObservationService;

    @Override
    protected List<AccountObservation> run(Request request) {
        if (request.accountObservationList.isEmpty()) {
            log.debug("No found account observation to create");
            return null;
        }

        return accountObservationService.saveAll(request.accountObservationList);
    }

    @Builder
    public record Request(@NotNull List<AccountObservation> accountObservationList) {

    }
}
