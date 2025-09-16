package com.kernotec.farm.activity.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.dto.entity.GroupMembershipDto;
import com.kernotec.farm.activity.jpa.dto.mapper.GroupMembershipDtoMapper;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.service.GroupMembershipService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupMembershipGetDtoCmd extends
    AbstractTransactionalRequiredCommand<GroupMembershipGetDtoCmd.Request, GroupMembershipDto>
{

    private final GroupMembershipService groupMembershipService;
    private final GroupMembershipDtoMapper groupMembershipDtoMapper;

    @Override
    protected GroupMembershipDto run(Request request) {
        GroupMembership groupMembership = groupMembershipService.findByIdThrow(
            request.getGroupMembershipId());
        return groupMembershipDtoMapper.toDto(groupMembership);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID groupMembershipId;
    }
}
