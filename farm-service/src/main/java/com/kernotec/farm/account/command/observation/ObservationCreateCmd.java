package com.kernotec.farm.account.command.observation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Observation;
import com.kernotec.farm.account.jpa.service.ObservationService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ObservationCreateCmd extends
    AbstractTransactionalRequiredCommand<ObservationCreateCmd.Request, UUID>
{

    private final ObservationService observationService;

    @Override
    protected UUID run(Request request) {
        Observation observation = new Observation();

        observation.setDescription(request.getDescription());
        observation.setDateObserved(request.getDateObserved());

        observation = observationService.save(observation);
        return observation.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String description;
        @NotNull
        private final ZonedDateTime dateObserved;
    }
}
