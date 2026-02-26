package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.account.command.account.extension.AccountExtensionCreateManyCmd;
import com.kernotec.farm.account.command.account.observation.AccountObservationCreateManyCmd;
import com.kernotec.farm.account.command.assigned.chip.AssignedChipCreateManyCmd;
import com.kernotec.farm.account.command.device.account.DeviceAccountCreateManyCmd;
import com.kernotec.farm.account.command.observation.ObservationCreateManyCmd;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import com.kernotec.farm.account.jpa.entity.AccountObservation;
import com.kernotec.farm.account.jpa.entity.AssignedChip;
import com.kernotec.farm.account.jpa.entity.DeviceAccount;
import com.kernotec.farm.account.jpa.entity.Observation;
import com.kernotec.farm.account.rest.dto.request.account.observation.AccountObservationCreateRequest;
import com.kernotec.farm.parametric.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.AssignAccountRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HandleAssignAccount implements HandleImport<AssignAccountRequest> {

    private final CsvImportCommon csvImportCommon;

    private final DeviceAccountCreateManyCmd deviceAccountCreateManyCmd;
    private final AssignedChipCreateManyCmd assignedChipCreateManyCmd;
    private final AccountExtensionCreateManyCmd accountExtensionCreateManyCmd;
    private final ObservationCreateManyCmd observationCreateManyCmd;
    private final AccountObservationCreateManyCmd accountObservationCreateManyCmd;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.ASSIGN_ACCOUNT;
    }

    @Override
    public Map<String, UUID> handle(AssignAccountRequest request) {
        Map<String, UUID> deviceMap = request.deviceMap();
        Map<String, UUID> chipMap = request.chipMap();
        Map<String, UUID> accountMap = request.accountMap();

        List<SocialConfig> configs = getSocialConfigs();

        List<DeviceAccount> deviceAccountListToSave = new ArrayList<>();
        List<AssignedChip> assignedChipListToSave = new ArrayList<>();
        List<AccountExtension> accountExtensionListToSave = new ArrayList<>();
        List<AccountObservationCreateRequest> accountObservationCreateRequestList = new ArrayList<>();

        for (ImportExcelDataDto dto : request.importExcelDataDtoBatch()) {
            UUID deviceId = deviceMap.get(dto.getDeviceNumber());

            UUID chipId = chipMap.get(
                csvImportCommon.getChipUniqueCode(dto.getPhoneNumber(), dto.getSequentialNumber()));

            String referenceEmail = dto.getEmailUsername();

            for (SocialConfig socialConfig : configs) {
                String username = socialConfig.userFn.apply(dto);
                String observation = socialConfig.obsFn.apply(dto);

                addAccountAssigments(AccountAddRequest.builder()
                    .username(username)
                    .accountType(socialConfig.type)
                    .accountMap(accountMap)
                    .addDeviceAccountFn(accountId -> deviceAccountListToSave.add(
                        getDeviceAccountEntity(deviceId, accountId)))
                    .addAssignedChipFn(accountId -> assignedChipListToSave.add(
                        getAssignedChipEntity(chipId, accountId)))
                    .addExtensionFn(accountId -> addReferenceEmail(
                        referenceEmail, accountId,
                        accountExtensionListToSave
                    ))
                    .addObservationFn(accountId -> addObservation(
                        observation, accountId,
                        accountObservationCreateRequestList
                    ))
                    .build());
            }

            if (dto.getIsHaveWhatsapp()) {
                addAccountAssigments(AccountAddRequest.builder()
                    .username(dto.getPhoneNumber())
                    .accountType(SocialNetworkEnum.WHATSAPP)
                    .accountMap(accountMap)
                    .addDeviceAccountFn(accountId -> deviceAccountListToSave.add(
                        getDeviceAccountEntity(deviceId, accountId)))
                    .addAssignedChipFn(accountId -> assignedChipListToSave.add(
                        getAssignedChipEntity(chipId, accountId)))
                    .build());
            }
        }

        saveDeviceAccounts(deviceAccountListToSave);

        saveAssignedChips(assignedChipListToSave);

        saveAccountExtensions(accountExtensionListToSave);

        saveAccountObservations(accountObservationCreateRequestList);

        return null;
    }

    private List<SocialConfig> getSocialConfigs() {
        return List.of(
            SocialConfig.builder()
                .type(SocialNetworkEnum.FACEBOOK)
                .userFn(ImportExcelDataDto::getFacebookUsername)
                .obsFn(ImportExcelDataDto::getFacebookObservation)
                .build(), SocialConfig.builder()
                .type(SocialNetworkEnum.TIKTOK)
                .userFn(ImportExcelDataDto::getTiktokUsername)
                .obsFn(ImportExcelDataDto::getTiktokObservation)
                .build(), SocialConfig.builder()
                .type(SocialNetworkEnum.X)
                .userFn(ImportExcelDataDto::getXUsername)
                .obsFn(dto -> null)
                .build(), SocialConfig.builder()
                .type(SocialNetworkEnum.CORREO)
                .userFn(ImportExcelDataDto::getEmailUsername)
                .obsFn(dto -> null)
                .build()
        );
    }

    private void addAccountAssigments(AccountAddRequest request)
    {
        if (request.username == null || request.username.equalsIgnoreCase("N/A")) {
            log.debug("facebook username is null, skipping facebook account assign");
            return;
        }

        UUID accountId = request.accountMap.get(
            csvImportCommon.getAccountUniqueCode(
                String.valueOf(request.accountType), request.username));

        request.addDeviceAccountFn.accept(accountId);
        request.addAssignedChipFn.accept(accountId);

        if (request.addExtensionFn != null) {
            request.addExtensionFn.accept(accountId);
        }
        if (request.addObservationFn != null) {
            request.addObservationFn.accept(accountId);
        }
    }

    private DeviceAccount getDeviceAccountEntity(UUID deviceId, UUID accountId) {
        var deviceAccount = new DeviceAccount();
        deviceAccount.setDeviceId(deviceId);
        deviceAccount.setAccountId(accountId);

        return deviceAccount;
    }

    private AssignedChip getAssignedChipEntity(UUID chipId, UUID accountId) {
        var assignedChip = new AssignedChip();
        assignedChip.setChipId(chipId);
        assignedChip.setAccountId(accountId);

        return assignedChip;
    }

    private void addReferenceEmail(String email, UUID accountId,
        List<AccountExtension> accountExtensionListToSave)
    {
        if (email == null) {
            log.debug("email is null, skipping account extension creation");
            return;
        }

        accountExtensionListToSave.add(getAccountExtensionEntity(email, accountId));
    }

    private AccountExtension getAccountExtensionEntity(String referenceEmail, UUID accountId) {
        var accountExtension = new AccountExtension();
        accountExtension.setReferenceEmail(referenceEmail);
        accountExtension.setAccountId(accountId);

        return accountExtension;
    }

    private void addObservation(String observation, UUID accountId,
        List<AccountObservationCreateRequest> accountObservationCreateRequestList)
    {
        if (observation == null) {
            log.debug("observation is null, skipping account observation creation");
            return;
        }

        accountObservationCreateRequestList.add(
            getAccountObservationCreateRequest(observation, accountId));
    }

    private AccountObservationCreateRequest getAccountObservationCreateRequest(String observation,
        UUID accountId)
    {
        var accountObservationCreateRequest = new AccountObservationCreateRequest();
        accountObservationCreateRequest.setObservation(observation);
        accountObservationCreateRequest.setAccountId(accountId);

        return accountObservationCreateRequest;
    }

    private void saveDeviceAccounts(List<DeviceAccount> deviceAccountList) {
        if (deviceAccountList.isEmpty()) {
            log.info("no device account to save");
            return;
        }

        deviceAccountCreateManyCmd.withRequest(DeviceAccountCreateManyCmd.Request.builder()
                .deviceAccountList(deviceAccountList)
                .build())
            .execute();
    }

    private void saveAssignedChips(List<AssignedChip> assignedChipList) {
        if (assignedChipList.isEmpty()) {
            log.info("no assigned chip to save");
            return;
        }

        assignedChipCreateManyCmd.withRequest(AssignedChipCreateManyCmd.Request.builder()
                .assignedChipList(assignedChipList)
                .build())
            .execute();
    }

    private void saveAccountExtensions(List<AccountExtension> accountExtensionList) {
        if (accountExtensionList.isEmpty()) {
            log.info("no account extension to save");
            return;
        }

        accountExtensionCreateManyCmd.withRequest(AccountExtensionCreateManyCmd.Request.builder()
                .accountExtensionList(accountExtensionList)
                .build())
            .execute();
    }

    private void saveAccountObservations(
        List<AccountObservationCreateRequest> accountObservationCreateRequestList)
    {
        if (accountObservationCreateRequestList.isEmpty()) {
            log.info("no account observation to save");
            return;
        }

        log.info(
            "account observation create request list size: {}",
            accountObservationCreateRequestList.size()
        );

        Map<String, Observation> createObservationRequestMap = accountObservationCreateRequestList.stream()
            .collect(
                Collectors.toMap(
                    AccountObservationCreateRequest::getObservation, this::getObservationEntity,
                    (existing, replacement) -> replacement
                ));

        log.info("create observation request map size: {} ", createObservationRequestMap.size());

        List<Observation> observationListSaved = observationCreateManyCmd.withRequest(
                ObservationCreateManyCmd.Request.builder()
                    .observationList(createObservationRequestMap.values()
                        .stream()
                        .toList())
                    .build())
            .execute();

        Map<String, UUID> observationMap = observationListSaved.stream()
            .collect(Collectors.toMap(Observation::getDescription, Observation::getId));

        List<AccountObservation> accountObservationListToSave = new ArrayList<>();

        for (AccountObservationCreateRequest accountObservationCreateRequest : accountObservationCreateRequestList) {
            AccountObservation accountObservation = getAccountObservationEntity(
                accountObservationCreateRequest.getAccountId(),
                observationMap.get(accountObservationCreateRequest.getObservation())
            );

            accountObservationListToSave.add(accountObservation);
        }

        accountObservationCreateManyCmd.withRequest(
                AccountObservationCreateManyCmd.Request.builder()
                    .accountObservationList(accountObservationListToSave)
                    .build())
            .execute();
    }

    private Observation getObservationEntity(AccountObservationCreateRequest observationRequest) {
        var observation = new Observation();
        observation.setDescription(observationRequest.getObservation());
        observation.setDateObserved(ZonedDateTime.now());

        return observation;
    }

    private AccountObservation getAccountObservationEntity(UUID accountId, UUID observationId) {
        var accountObservation = new AccountObservation();
        accountObservation.setAccountId(accountId);
        accountObservation.setObservationId(observationId);

        return accountObservation;
    }

    @Builder
    public record AccountAddRequest(String username, SocialNetworkEnum accountType,
                                    Map<String, UUID> accountMap, Consumer<UUID> addDeviceAccountFn,
                                    Consumer<UUID> addAssignedChipFn, Consumer<UUID> addExtensionFn,
                                    Consumer<UUID> addObservationFn)
    {

    }

    @Builder
    public record SocialConfig(SocialNetworkEnum type, Function<ImportExcelDataDto, String> userFn,
                               Function<ImportExcelDataDto, String> obsFn)
    {

    }
}
