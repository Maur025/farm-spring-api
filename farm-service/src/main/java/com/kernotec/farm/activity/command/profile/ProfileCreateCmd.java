package com.kernotec.farm.activity.command.profile;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.activity.jpa.service.ProfileService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileCreateCmd extends
    AbstractTransactionalRequiredCommand<ProfileCreateCmd.Request, UUID>
{

    private final ProfileService profileService;

    @Override
    protected UUID run(Request request) {
        Profile profile = new Profile();

        profile.setName(request.getName());
        profile.setRegionId(request.getRegionId());
        profile.setPublishingContextId(request.getPublishingContextId());

        profile = profileService.save(profile);
        return profile.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        private final UUID regionId;
        @NotNull
        private final UUID publishingContextId;
    }
}
