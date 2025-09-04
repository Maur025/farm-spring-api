package com.kernotec.farm.activity.command.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.jpa.service.GroupService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupCreateCmd extends
    AbstractTransactionalRequiredCommand<GroupCreateCmd.Request, UUID>
{

    private final GroupService groupService;

    @Override
    protected UUID run(Request request) {
        Group group = new Group();

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setRegionId(request.getRegionId());

        group = groupService.save(group);
        return group.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        @NotBlank
        private final String name;
        @NotNull
        private final UUID regionId;
        private final String description;
    }
}
