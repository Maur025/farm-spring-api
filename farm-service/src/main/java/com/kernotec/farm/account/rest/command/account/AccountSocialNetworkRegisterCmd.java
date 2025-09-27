package com.kernotec.farm.account.rest.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountCreateCmd;
import com.kernotec.farm.account.command.account.extension.AccountExtensionCreateCmd;
import com.kernotec.farm.account.command.account.observation.AccountObservationCreateCmd;
import com.kernotec.farm.account.command.assigned.chip.AssignedChipCreateCmd;
import com.kernotec.farm.account.command.device.account.DeviceAccountCreateCmd;
import com.kernotec.farm.account.command.observation.ObservationCreateCmd;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.parametric.jpa.service.SocialNetworkService;
import com.kernotec.farm.util.LinkUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountSocialNetworkRegisterCmd extends
    AbstractTransactionalRequiredCommand<AccountSocialNetworkRegisterCmd.Request, Void>
{

    private final SocialNetworkService socialNetworkService;

    private final AccountCreateCmd accountCreateCmd;
    private final DeviceAccountCreateCmd deviceAccountCreateCmd;
    private final AssignedChipCreateCmd assignedChipCreateCmd;
    private final ObservationCreateCmd observationCreateCmd;
    private final AccountObservationCreateCmd accountObservationCreateCmd;
    private final AccountExtensionCreateCmd accountExtensionCreateCmd;
    private final LinkUtil linkUtil;

    @Override
    protected Void run(Request request) {
        SocialNetwork socialNetwork = socialNetworkService.findByCodeThrow(
            request.getSocialNetworkCode()
                .toString());

        String identityUsername = switch (SocialNetworkEnum.fromValue(socialNetwork.getCode())) {
            case FACEBOOK -> linkUtil.getIdentityFacebookOfLink(request.getAccountLink());
            case TIKTOK -> {
                log.info("TikTok Account Link: {}", request.getAccountLink());
                yield null;
            }
            default -> null;
        };

        UUID accountId = accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                .username(request.getUsername())
                .password(request.getPassword() == null ? "N/A" : request.getPassword())
                .personId(request.getPersonId())
                .socialNetworkId(socialNetwork.getId())
                .type(request.getAccountType())
                .accountLink(request.getAccountLink())
                .identityUsername(identityUsername)
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

        if (request.getReferenceEmail() != null && !request.getReferenceEmail()
            .isBlank())
        {
            accountExtensionCreateCmd.withRequest(AccountExtensionCreateCmd.Request.builder()
                    .accountId(accountId)
                    .referenceEmail(request.getReferenceEmail())
                    .build())
                .execute();
        }

        if (request.getObservation() == null || request.getObservation()
            .isBlank())
        {
            return null;
        }

        UUID observationId = observationCreateCmd.withRequest(ObservationCreateCmd.Request.builder()
                .description(request.getObservation())
                .dateObserved(ZonedDateTime.now())
                .build())
            .execute();

        accountObservationCreateCmd.withRequest(AccountObservationCreateCmd.Request.builder()
                .accountId(accountId)
                .observationId(observationId)
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
        private final String observation;
        private final String referenceEmail;
        private final String accountLink;
    }
}
