package com.kernotec.farm.account.command.observation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Observation;
import com.kernotec.farm.account.jpa.service.ObservationService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ObservationCreateManyCmd extends
    AbstractTransactionalRequiredCommand<ObservationCreateManyCmd.Request, List<Observation>>
{

    private final ObservationService observationService;

    @Override
    protected List<Observation> run(Request request) {
        if (request.observationList.isEmpty()) {
            log.debug("No observations to create");
            return null;
        }

        return observationService.saveAll(request.observationList);
    }

    @Builder
    public record Request(@NotNull List<Observation> observationList) {

    }
}
