package com.kernotec.farm.rest.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.account.AccountCreateCmd;
import com.kernotec.farm.command.assigned.chip.AssignedChipCreateCmd;
import com.kernotec.farm.command.device.account.DeviceAccountCreateCmd;
import com.kernotec.farm.jpa.entity.SocialNetwork;
import com.kernotec.farm.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.jpa.service.SocialNetworkService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountSocialNetworkRegisterCmd extends
    AbstractTransactionalRequiredCommand<AccountSocialNetworkRegisterCmd.Request, Void>
{

    private final SocialNetworkService socialNetworkService;

    private final AccountCreateCmd accountCreateCmd;
    private final DeviceAccountCreateCmd deviceAccountCreateCmd;
    private final AssignedChipCreateCmd assignedChipCreateCmd;

    @Override
    protected Void run(Request request) {
        SocialNetwork socialNetwork = socialNetworkService.findByCodeThrow(
            request.getSocialNetworkCode()
                .toString());

        UUID accountId = accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                .username(request.getUsername())
                .password(request.getPassword() == null ? "N/A" : request.getPassword())
                .personId(request.getPersonId())
                .socialNetworkId(socialNetwork.getId())
                .type(request.getAccountType())
                .build())
            .execute();

        deviceAccountCreateCmd.withRequest(DeviceAccountCreateCmd.Request.builder()
                .accountId(accountId)
                .deviceId(request.getDeviceId())
                .build())
            .execute();

        assignedChipCreateCmd.withRequest(AssignedChipCreateCmd.Request.builder()
                .chipId(request.getChipId())
                .accountId(accountId)
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final SocialNetworkEnum socialNetworkCode;
        @NotNull
        private final String username;
        private final String password;
        @NotNull
        private final UUID chipId;
        @NotNull
        private final UUID personId;
        @NotNull
        private final UUID deviceId;
        @NotNull
        private final AccountTypeEnum accountType;
    }
}
