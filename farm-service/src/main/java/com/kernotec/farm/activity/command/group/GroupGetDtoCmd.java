package com.kernotec.farm.activity.command.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.activity.jpa.dto.mapper.GroupDtoMapper;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.jpa.service.GroupService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupGetDtoCmd extends
    AbstractTransactionalRequiredCommand<GroupGetDtoCmd.Request, GroupDto>
{

    private final GroupService groupService;
    private final GroupDtoMapper groupDtoMapper;

    @Override
    protected GroupDto run(Request request) {
        Group group = groupService.findByIdThrow(request.getGroupId());
        return groupDtoMapper.toDto(group);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID groupId;
    }
}
