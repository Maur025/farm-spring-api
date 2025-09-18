package com.kernotec.farm.activity.rest.command.profile;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.profile.ProfileCreateCmd;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.activity.jpa.service.ProfileService;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileGetOrCreateCmd extends
    AbstractTransactionalRequiredCommand<ProfileGetOrCreateCmd.Request, UUID>
{

    private final ProfileCreateCmd profileCreateCmd;
    private final ProfileService profileService;

    @Override
    protected UUID run(Request request) {

        Optional<Profile> profile = profileService.findByName(request.getName());

        return profile.map(Profile::getId)
            .orElseGet(() -> profileCreateCmd.withRequest(ProfileCreateCmd.Request.builder()
                    .name(request.getName())
                    .regionId(request.getRegionId())
                    .publishingContextId(request.getPublishingContextId())
                    .build())
                .execute());
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final UUID publishingContextId;
        private final UUID regionId;
    }
}
