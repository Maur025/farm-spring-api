package com.kernotec.farm.account.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountCreateCmd;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.command.account.AccountUpdateCmd;
import com.kernotec.farm.account.command.assigned.chip.AssignedChipCreateCmd;
import com.kernotec.farm.account.command.device.account.DeviceAccountCreateCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.rest.command.person.PersonFindOrCreateCmd;
import com.kernotec.farm.account.rest.dto.request.account.AccountReplaceRequest;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountReplaceRequestCmd extends
    AbstractTransactionalRequiredCommand<AccountReplaceRequestCmd.Request, UUID>
{

    private final AccountGetDtoCmd accountGetDtoCmd;
    private final AccountCreateCmd accountCreateCmd;
    private final AssignedChipCreateCmd assignedChipCreateCmd;
    private final DeviceAccountCreateCmd deviceAccountCreateCmd;
    private final AccountUpdateCmd accountUpdateCmd;
    private final PersonFindOrCreateCmd personFindOrCreateCmd;

    @Override
    protected UUID run(Request request) {
        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(request.getAccountId())
                .build())
            .execute();

        if (accountDto.getIsEnabled()
            .equals(Boolean.FALSE))
        {
            throw new IllegalArgumentException("Account is already disabled and replaced");
        }

        accountUpdateCmd.withRequest(AccountUpdateCmd.Request.builder()
                .accountId(request.getAccountId())
                .isEnabled(false)
                .build())
            .execute();

        AccountReplaceRequest replaceRequest = request.getReplaceRequest();

        DeviceDto deviceDto = accountDto.getDevices()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        ChipDto chipDto = accountDto.getChips()
            .stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Chip not found"));

        UUID personId;
        if (replaceRequest.getReplacePerson()) {
            PersonDto personDto = personFindOrCreateCmd.withRequest(
                    PersonFindOrCreateCmd.Request.builder()
                        .name(replaceRequest.getFakeName())
                        .lastName(replaceRequest.getFakeLastName())
                        .build())
                .execute();

            personId = personDto.getId();
        } else {
            personId = accountDto.getPersonId();
        }

        UUID newAccountId = accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                .username(replaceRequest.getUsername())
                .password(replaceRequest.getPassword())
                .personId(personId)
                .socialNetworkId(accountDto.getSocialNetworkId())
                .type(AccountTypeEnum.INTERNAL)
                .build())
            .execute();

        assignedChipCreateCmd.withRequest(AssignedChipCreateCmd.Request.builder()
                .chipId(chipDto.getId())
                .accountId(newAccountId)
                .build())
            .execute();

        deviceAccountCreateCmd.withRequest(DeviceAccountCreateCmd.Request.builder()
                .deviceId(deviceDto.getId())
                .accountId(newAccountId)
                .build())
            .execute();

        return newAccountId;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountId;

        @NotNull
        private final AccountReplaceRequest replaceRequest;
    }
}
