package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.account.command.account.AccountCreateManyCmd;
import com.kernotec.farm.account.command.assigned.chip.AssignedChipCreateManyCmd;
import com.kernotec.farm.account.command.device.account.DeviceAccountCreateManyCmd;
import com.kernotec.farm.account.command.person.PersonCreateManyCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoFlatMapper;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import com.kernotec.farm.account.jpa.entity.AssignedChip;
import com.kernotec.farm.account.jpa.entity.DeviceAccount;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.AccountService;
import com.kernotec.farm.account.jpa.service.PersonService;
import com.kernotec.farm.inventory.command.chip.ChipCreateManyCmd;
import com.kernotec.farm.inventory.command.device.DeviceCreateManyCmd;
import com.kernotec.farm.inventory.command.device.connection.DeviceConnectionCreateManyCmd;
import com.kernotec.farm.inventory.command.device.imei.DeviceImeiCreateManyCmd;
import com.kernotec.farm.inventory.command.farm.FarmCreateManyCmd;
import com.kernotec.farm.inventory.command.registration.person.RegistrationPersonCreateManyCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.ChipDtoMapper;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.enums.FarmTypeEnum;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.inventory.jpa.service.RegistrationPersonService;
import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import com.kernotec.farm.parametric.jpa.dto.mapper.SocialNetworkDtoFlatMapper;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.parametric.jpa.service.OperatorService;
import com.kernotec.farm.parametric.jpa.service.SocialNetworkService;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import com.kernotec.farm.util.LinkUtil;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportCsvPersistBatchInDbCmd extends
    AbstractTransactionalRequiredCommand<ImportCsvPersistBatchInDbCmd.Request, Void>
{

    private final FarmService farmService;
    private final FarmCreateManyCmd farmCreateManyCmd;
    private final DeviceService deviceService;
    private final DeviceDtoMapper deviceDtoMapper;
    private final DeviceCreateManyCmd deviceCreateManyCmd;
    private final DeviceConnectionCreateManyCmd deviceConnectionCreateManyCmd;
    private final DeviceImeiCreateManyCmd deviceImeiCreateManyCmd;
    private final PersonService personService;
    private final PersonCreateManyCmd personCreateManyCmd;
    private final OperatorService operatorService;
    private final RegistrationPersonService registrationPersonService;
    private final RegistrationPersonCreateManyCmd registrationPersonCreateManyCmd;
    private final ChipCreateManyCmd chipCreateManyCmd;
    private final ChipDtoMapper chipDtoMapper;
    private final SocialNetworkService socialNetworkService;
    private final SocialNetworkDtoFlatMapper socialNetworkDtoFlatMapper;
    private final LinkUtil linkUtil;
    private final AccountService accountService;
    private final AccountDtoFlatMapper accountDtoFlatMapper;
    private final AccountCreateManyCmd accountCreateManyCmd;
    private final DeviceAccountCreateManyCmd deviceAccountCreateManyCmd;
    private final AssignedChipCreateManyCmd assignedChipCreateManyCmd;

    @Override
    protected Void run(Request request) {
        if (request.importExcelDataDtoList.isEmpty()) {
            return null;
        }

        Map<String, UUID> farmMap = getFarmMap(request.importExcelDataDtoList);

        Map<String, UUID> deviceMap = getDeviceMap(request.importExcelDataDtoList, farmMap);

        Map<String, UUID> personMap = getPersonMap(request.importExcelDataDtoList);

        Map<String, UUID> personRegisterMap = getPersonRegisterMap(request.importExcelDataDtoList);

        log.info("PERSON REGISTER MAP SIZE: {}", personRegisterMap.size());

        Map<OperatorCodeEnum, UUID> operatorMap = getOperatorMap(request.importExcelDataDtoList);

        Map<String, UUID> chipMap = getChipMap(
            request.importExcelDataDtoList, operatorMap, personRegisterMap, deviceMap);

        Map<String, UUID> socialNetworkMap = getSocialNetworkMap();
        Map<UUID, String> inverseSocialNetworkMap = socialNetworkMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        log.info("chip map size: {}", chipMap.size());

        Map<String, UUID> accountMap = getAccountMap(
            request.importExcelDataDtoList, personMap, socialNetworkMap, inverseSocialNetworkMap);

        return null;
    }

    private void assignAccounts(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> accountMap, Map<String, UUID> deviceMap, Map<String, UUID> chipMap)
    {
        List<DeviceAccount> deviceAccountListToSave = new ArrayList<>();
        List<AssignedChip> assignedChipListToSave = new ArrayList<>();
        List<AccountExtension> accountExtensionListToSave = new ArrayList<>();
        // List<> DTO to observations

        for (ImportExcelDataDto dto : importExcelDataDtoBatch) {
            UUID deviceId = deviceMap.get(dto.getDeviceNumber());

            UUID chipId = chipMap.get(
                getChipUniqueCode(dto.getPhoneNumber(), dto.getSequentialNumber()));

            if (dto.getFacebookUsername() != null) {
                UUID accountId = accountMap.get(
                    getAccountUniqueCode(
                        SocialNetworkEnum.FACEBOOK.toString(),
                        dto.getFacebookUsername()
                    ));

                deviceAccountListToSave.add(getDeviceAccountEntity(deviceId, accountId));
                assignedChipListToSave.add(getAssignedChipEntity(chipId, accountId));

                if (dto.getEmailUsername() != null) {
                    accountExtensionListToSave.add(
                        getAccountExtensionEntity(dto.getEmailUsername(), accountId));
                }
            }

            if (dto.getTiktokUsername() != null) {
                UUID accountId = accountMap.get(
                    getAccountUniqueCode(
                        SocialNetworkEnum.TIKTOK.toString(),
                        dto.getTiktokUsername()
                    ));

                deviceAccountListToSave.add(getDeviceAccountEntity(deviceId, accountId));
                assignedChipListToSave.add(getAssignedChipEntity(chipId, accountId));

                if (dto.getEmailUsername() != null) {
                    accountExtensionListToSave.add(
                        getAccountExtensionEntity(dto.getEmailUsername(), accountId));
                }
            }

            if (dto.getXUsername() != null) {
                UUID accountId = accountMap.get(
                    getAccountUniqueCode(SocialNetworkEnum.X.toString(), dto.getXUsername()));

                deviceAccountListToSave.add(getDeviceAccountEntity(deviceId, accountId));
                assignedChipListToSave.add(getAssignedChipEntity(chipId, accountId));

                if (dto.getEmailUsername() != null) {
                    accountExtensionListToSave.add(
                        getAccountExtensionEntity(dto.getEmailUsername(), accountId));
                }
            }

            if (dto.getIsHaveWhatsapp() && dto.getPhoneNumber() != null && !dto.getPhoneNumber()
                .equalsIgnoreCase("N/A"))
            {
                UUID accountId = accountMap.get(
                    getAccountUniqueCode(
                        SocialNetworkEnum.WHATSAPP.toString(), dto.getPhoneNumber()));

                deviceAccountListToSave.add(getDeviceAccountEntity(deviceId, accountId));
                assignedChipListToSave.add(getAssignedChipEntity(chipId, accountId));
            }

            if (dto.getEmailUsername() != null) {
                UUID accountId = accountMap.get(
                    getAccountUniqueCode(
                        SocialNetworkEnum.CORREO.toString(),
                        dto.getEmailUsername()
                    ));

                deviceAccountListToSave.add(getDeviceAccountEntity(deviceId, accountId));
                assignedChipListToSave.add(getAssignedChipEntity(chipId, accountId));
            }
        }

        deviceAccountCreateManyCmd.withRequest(DeviceAccountCreateManyCmd.Request.builder()
                .deviceAccountList(deviceAccountListToSave)
                .build())
            .execute();

        assignedChipCreateManyCmd.withRequest(AssignedChipCreateManyCmd.Request.builder()
                .assignedChipList(assignedChipListToSave)
                .build())
            .execute();


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

    private AccountExtension getAccountExtensionEntity(String referenceEmail, UUID accountId) {
        var accountExtension = new AccountExtension();
        accountExtension.setReferenceEmail(referenceEmail);
        accountExtension.setAccountId(accountId);

        return accountExtension;
    }

    private Map<String, UUID> getSocialNetworkMap() {
        Page<SocialNetwork> socialNetworkPage = socialNetworkService.findAll(
            PageableUtil.of(0, 20, "name", false));

        List<SocialNetworkDto> socialNetworkDtoList = socialNetworkDtoFlatMapper.toDto(
            socialNetworkPage.getContent());

        return socialNetworkDtoList.stream()
            .collect(Collectors.toMap(SocialNetworkDto::getCode, SocialNetworkDto::getId));
    }

    private Map<String, UUID> getAccountMap(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> personMap, Map<String, UUID> socialNetworkMap,
        Map<UUID, String> inverseSocialNetworkMap)
    {
        Map<String, Account> accountMemoryMap = importExcelDataDtoBatch.stream()
            .flatMap(dto -> {
                List<Account> accounts = new ArrayList<>();

                if (dto.getFacebookUsername() != null) {
                    UUID socialNetworkId = socialNetworkMap.get(
                        SocialNetworkEnum.FACEBOOK.toString());

                    Account facebookAccount = getAccountEntity(
                        dto.getFacebookUsername(),
                        dto.getFacebookPassword(), socialNetworkId, dto.getFacebookAccountLink(),
                        dto, personMap, inverseSocialNetworkMap
                    );

                    accounts.add(facebookAccount);
                }

                if (dto.getTiktokUsername() != null) {
                    UUID socialNetworkId = socialNetworkMap.get(
                        SocialNetworkEnum.TIKTOK.toString());

                    Account tiktokAccount = getAccountEntity(
                        dto.getTiktokUsername(), dto.getTiktokPassword(), socialNetworkId, dto,
                        personMap
                    );

                    accounts.add(tiktokAccount);
                }

                if (dto.getXUsername() != null) {
                    UUID socialNetworkId = socialNetworkMap.get(SocialNetworkEnum.X.toString());

                    Account xAccount = getAccountEntity(
                        dto.getXUsername(), dto.getXPassword(), socialNetworkId, dto, personMap);

                    accounts.add(xAccount);
                }

                if (dto.getIsHaveWhatsapp() && dto.getPhoneNumber() != null && !dto.getPhoneNumber()
                    .equalsIgnoreCase("N/A"))
                {
                    UUID socialNetworkId = socialNetworkMap.get(
                        SocialNetworkEnum.WHATSAPP.toString());

                    Account whatsappAccount = getAccountEntity(
                        dto.getPhoneNumber(), null, socialNetworkId, dto, personMap);

                    accounts.add(whatsappAccount);
                }

                if (dto.getEmailUsername() != null) {
                    UUID socialNetworkId = socialNetworkMap.get(
                        SocialNetworkEnum.CORREO.toString());

                    Account emailAccount = getAccountEntity(
                        dto.getEmailUsername(), dto.getEmailPassword(), socialNetworkId, dto,
                        personMap
                    );

                    accounts.add(emailAccount);
                }

                return accounts.stream();
            })
            .collect(Collectors.toMap(
                account -> getAccountUniqueCode(
                    inverseSocialNetworkMap.get(account.getSocialNetworkId()),
                    account.getUsername()
                ), account -> account
            ));

        Set<String> facebookAccountLinkSet = new HashSet<>();
        Set<String> accountUsernameSet = new HashSet<>();

        for (Account account : accountMemoryMap.values()) {
            String socialNetworkCodeString = inverseSocialNetworkMap.get(
                account.getSocialNetworkId());

            if (SocialNetworkEnum.FACEBOOK.equals(
                SocialNetworkEnum.fromValue(socialNetworkCodeString)))
            {
                facebookAccountLinkSet.add(account.getAccountLink());
            } else {
                accountUsernameSet.add(account.getUsername());
            }
        }

        Page<Account> accountFacebookMatchPage = accountService.findAllByAccountLinkIn(
            facebookAccountLinkSet);

        List<AccountDto> accountFacebookMatchDtoList = accountDtoFlatMapper.toDto(
            accountFacebookMatchPage.getContent());

        Map<String, UUID> facebookAccountMatchMap = accountFacebookMatchDtoList.stream()
            .collect(Collectors.toMap(
                accountDto -> getAccountUniqueCode(
                    inverseSocialNetworkMap.get(accountDto.getSocialNetworkId()),
                    accountDto.getUsername()
                ), AccountDto::getId
            ));

        Page<Account> accountMatchPage = accountService.findAllByUsernameInAndType(
            accountUsernameSet, AccountTypeEnum.INTERNAL);
        List<AccountDto> accountMatchDtoList = accountDtoFlatMapper.toDto(
            accountMatchPage.getContent());

        Map<String, UUID> accountMatchMap = accountMatchDtoList.stream()
            .collect(Collectors.toMap(
                accountDto -> getAccountUniqueCode(
                    inverseSocialNetworkMap.get(accountDto.getSocialNetworkId()),
                    accountDto.getUsername()
                ), AccountDto::getId
            ));

        List<Account> accountListToSave = new ArrayList<>();

        for (Account account : accountMemoryMap.values()) {
            SocialNetworkEnum socialNetworkCode = SocialNetworkEnum.fromValue(
                inverseSocialNetworkMap.get(account.getSocialNetworkId()));

            if (SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && facebookAccountMatchMap.containsKey(
                getAccountUniqueCode(socialNetworkCode.toString(), account.getUsername())))
            {
                continue;
            }

            if (!SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && accountMatchMap.containsKey(
                getAccountUniqueCode(socialNetworkCode.toString(), account.getUsername())))
            {
                continue;
            }

            accountListToSave.add(account);
        }

        if (!accountListToSave.isEmpty()) {
            List<Account> accountListSaved = accountCreateManyCmd.withRequest(
                    AccountCreateManyCmd.Request.builder()
                        .accountList(accountListToSave)
                        .build())
                .execute();

            for (Account account : accountListSaved) {
                String socialNetworkCodeString = inverseSocialNetworkMap.get(
                    account.getSocialNetworkId());

                accountMatchMap.put(
                    getAccountUniqueCode(socialNetworkCodeString, account.getUsername()),
                    account.getId()
                );
            }
        }

        facebookAccountMatchMap.forEach((key, value) -> accountMatchMap.merge(
            key, value,
            (existing, replacement) -> replacement
        ));

        return accountMatchMap;
    }

    private String getAccountUniqueCode(String socialNetworkCode, String username) {
        return String.format("%s|%s", socialNetworkCode, username);
    }

    private Account getAccountEntity(String username, String password, UUID socialNetworkId,
        String accountLink, ImportExcelDataDto dto, Map<String, UUID> personMap,
        Map<UUID, String> inverseSocialNetworkMap)
    {
        String socialNetworkCode = inverseSocialNetworkMap.get(socialNetworkId);
        String identityUsername = switch (SocialNetworkEnum.fromValue(socialNetworkCode)) {
            case FACEBOOK -> linkUtil.getIdentityFacebookOfLink(accountLink);
            case TIKTOK -> {
                log.info("Tiktok account link:{}", accountLink);
                yield null;
            }
            default -> null;
        };

        return getAccountEntity(
            username, password, accountLink, identityUsername, socialNetworkId, dto, personMap);
    }

    private Account getAccountEntity(String username, String password, UUID socialNetworkId,
        ImportExcelDataDto dto, Map<String, UUID> personMap)
    {
        return getAccountEntity(username, password, null, null, socialNetworkId, dto, personMap);
    }

    private Account getAccountEntity(String username, String password, String accountLink,
        String identityUsername, UUID socialNetworkId, ImportExcelDataDto dto,
        Map<String, UUID> personMap)
    {
        var account = new Account();

        account.setUsername(username);
        account.setPassword(password == null ? "N/A" : password);
        account.setPersonId(
            personMap.get(getPersonCode(dto.getPersonFakeName(), dto.getPersonFakeLastName())));
        account.setSocialNetworkId(socialNetworkId);
        account.setType(AccountTypeEnum.INTERNAL);
        account.setIsEnabled(true);
        account.setAccountLink(accountLink);
        account.setIdentityUsername(identityUsername);

        return account;
    }

    private Map<OperatorCodeEnum, UUID> getOperatorMap(
        List<ImportExcelDataDto> importExcelDataDtoBatch)
    {
        Set<OperatorCodeEnum> operatorCodeSet = importExcelDataDtoBatch.stream()
            .map(dto -> OperatorCodeEnum.fromValue(dto.getOperator()))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (operatorCodeSet.isEmpty()) {
            return Map.of();
        }

        return operatorService.findAllByCodeIn(operatorCodeSet)
            .getContent()
            .stream()
            .collect(Collectors.toMap(Operator::getCode, Operator::getId));
    }

    private Map<String, UUID> getPersonRegisterMap(List<ImportExcelDataDto> importExcelDataDtoBatch)
    {
        Map<String, Person> personRegisterMap = importExcelDataDtoBatch.stream()
            .collect(Collectors.toMap(
                dto -> getPersonCode(dto.getPersonRegisterName(), dto.getPersonRegisterLastName()),
                dto -> getPersonEntity(
                    dto.getPersonRegisterName(),
                    dto.getPersonRegisterLastName()
                ), (existing, replacement) -> replacement
            ));

        log.info("person register map size: {}", personRegisterMap.size());

        List<String> personNameAndLastNames = personRegisterMap.values()
            .stream()
            .map(person -> getPersonCode(person.getName(), person.getLastName()))
            .toList();

        Map<String, UUID> personRegisterMatchMap = personService.findAllByNameAndLastNameInList(
                personNameAndLastNames)
            .stream()
            .collect(
                Collectors.toMap(
                    person -> getPersonCode(person.getName(), person.getLastName()),
                    Person::getId
                ));

        log.info("person register match map size: {}", personRegisterMatchMap.size());

        List<Person> personListToSave = new ArrayList<>();
        for (Person person : personRegisterMap.values()) {
            if (personRegisterMatchMap.containsKey(
                getPersonCode(person.getName(), person.getLastName())))
            {
                continue;
            }

            personListToSave.add(person);
        }

        if (!personListToSave.isEmpty()) {
            List<Person> personListSaved = personCreateManyCmd.withRequest(
                    PersonCreateManyCmd.Request.builder()
                        .personList(personListToSave)
                        .build())
                .execute();

            for (Person person : personListSaved) {
                personRegisterMatchMap.put(
                    getPersonCode(person.getName(), person.getLastName()), person.getId());
            }
        }

        Map<UUID, String> inversePersonRegisterMatchMap = personRegisterMatchMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        Map<UUID, RegistrationPerson> registrationPersonMemoryMap = personRegisterMatchMap.values()
            .stream()
            .collect(Collectors.toMap(personId -> personId, this::getRegistrationPersonEntity));

        log.info("registration person memory map size: {}", registrationPersonMemoryMap.size());

        Set<UUID> personIds = new HashSet<>(personRegisterMatchMap.values());

        Map<UUID, UUID> registrationPersonMatchMap = registrationPersonService.findAllByPersonIdIn(
                personIds)
            .stream()
            .collect(Collectors.toMap(RegistrationPerson::getPersonId, RegistrationPerson::getId));

        log.info("registration person match map size: {}", registrationPersonMatchMap.size());

        List<RegistrationPerson> registrationPersonListToSave = new ArrayList<>();

        for (RegistrationPerson registrationPerson : registrationPersonMemoryMap.values()) {
            if (registrationPersonMatchMap.containsKey(registrationPerson.getPersonId())) {
                continue;
            }

            registrationPersonListToSave.add(registrationPerson);
        }

        if (!registrationPersonListToSave.isEmpty()) {
            List<RegistrationPerson> registrationPersonListSaved = registrationPersonCreateManyCmd.withRequest(
                    RegistrationPersonCreateManyCmd.Request.builder()
                        .registrationPersonList(registrationPersonListToSave)
                        .build())
                .execute();

            for (RegistrationPerson registrationPerson : registrationPersonListSaved) {
                registrationPersonMatchMap.put(
                    registrationPerson.getPersonId(), registrationPerson.getId());
            }
        }

        return registrationPersonMatchMap.entrySet()
            .stream()
            .filter(entry -> inversePersonRegisterMatchMap.containsKey(entry.getKey()))
            .collect(Collectors.toMap(
                entry -> inversePersonRegisterMatchMap.get(entry.getKey()),
                Map.Entry::getValue
            ));
    }

    private RegistrationPerson getRegistrationPersonEntity(UUID personId)
    {
        var registrationPerson = new RegistrationPerson();

        registrationPerson.setPersonId(personId);
        registrationPerson.setDocumentNumber("N/A");

        return registrationPerson;
    }

    private Map<String, UUID> getChipMap(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<OperatorCodeEnum, UUID> operatorMap, Map<String, UUID> registrationPersonMap,
        Map<String, UUID> deviceMap)
    {
        List<Chip> chipListToSave = importExcelDataDtoBatch.stream()
            .map(dto -> getChipEntity(dto, operatorMap, registrationPersonMap, deviceMap))
            .toList();

        if (chipListToSave.isEmpty()) {
            return Map.of();
        }

        List<Chip> chipListSaved = chipCreateManyCmd.withRequest(ChipCreateManyCmd.Request.builder()
                .chipList(chipListToSave)
                .build())
            .execute();

        List<ChipDto> chipDtoList = chipDtoMapper.toDto(chipListSaved);

        return chipDtoList.stream()
            .collect(Collectors.toMap(ChipDto::getPhoneNumber, ChipDto::getId));
    }

    private Chip getChipEntity(ImportExcelDataDto dto, Map<OperatorCodeEnum, UUID> operatorMap,
        Map<String, UUID> registrationPersonMap, Map<String, UUID> deviceMap)
    {
        UUID registrationPersonId = null;

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber()
            .isBlank() && !dto.getPhoneNumber()
            .equalsIgnoreCase("N/A"))

        {
            registrationPersonId = registrationPersonMap.get(
                getPersonCode(dto.getPersonRegisterName(), dto.getPersonRegisterLastName()));
        }

        String chipNumber = dto.getPhoneNumber();

        if (registrationPersonId == null) {
            chipNumber = getChipUniqueCode(dto.getPhoneNumber(), dto.getSequentialNumber());
        }

        var chip = new Chip();

        chip.setPhoneNumber(chipNumber);
        chip.setDeviceInside(
            dto.getIsChipInDevice() == null ? Boolean.FALSE : dto.getIsChipInDevice());
        chip.setOperatorId(dto.getOperator() == null ? null
            : operatorMap.get(OperatorCodeEnum.fromValue(dto.getOperator())));
        chip.setRegistrationPersonId(registrationPersonId);
        chip.setDeviceId(deviceMap.get(dto.getDeviceNumber()));

        return chip;
    }

    private String getChipUniqueCode(String phoneNumber, String index) {
        if (phoneNumber == null) {
            return index;
        }

        if (phoneNumber.equalsIgnoreCase("N/A")) {
            return String.format("%s|%s", phoneNumber, index);

        }

        return phoneNumber;
    }

    private Map<String, UUID> getPersonMap(List<ImportExcelDataDto> importExcelDataDtoBatch) {
        Map<String, Person> personFakeMap = importExcelDataDtoBatch.stream()
            .collect(Collectors.toMap(
                dto -> getPersonCode(dto.getPersonFakeName(), dto.getPersonFakeLastName()),
                dto -> getPersonEntity(dto.getPersonFakeName(), dto.getPersonFakeLastName()),
                (existing, replacement) -> existing
            ));

        log.info("person fake map size: {}", personFakeMap.size());

        List<String> nameAndLastNameSearchList = personFakeMap.values()
            .stream()
            .map(person -> getPersonCode(person.getName(), person.getLastName()))
            .toList();

        Map<String, UUID> personMatchMap = personService.findAllByNameAndLastNameInList(
                nameAndLastNameSearchList)
            .getContent()
            .stream()
            .collect(
                Collectors.toMap(
                    person -> getPersonCode(person.getName(), person.getLastName()),
                    Person::getId
                ));

        log.info("person match map size: {}", personMatchMap.size());

        List<Person> personListToSave = new ArrayList<>();

        for (Person person : personFakeMap.values()) {
            if (personMatchMap.containsKey(getPersonCode(person.getName(), person.getLastName()))) {
                continue;
            }

            personListToSave.add(person);
        }

        if (personListToSave.isEmpty()) {
            return personMatchMap;
        }

        List<Person> personListSaved = personCreateManyCmd.withRequest(
                PersonCreateManyCmd.Request.builder()
                    .personList(personListToSave)
                    .build())
            .execute();

        for (Person person : personListSaved) {
            personMatchMap.put(
                getPersonCode(person.getName(), person.getLastName()), person.getId());
        }

        return personMatchMap;
    }

    private String getPersonCode(String name, String lastName) {
        return String.format("%s|%s", name, lastName);
    }

    private Person getPersonEntity(String personName, String personLastName) {
        var person = new Person();

        person.setName(personName);
        person.setLastName(personLastName);

        return person;
    }

    private Map<String, UUID> getDeviceMap(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> farmMap)
    {
        Map<String, Device> deviceMemoryMap = importExcelDataDtoBatch.stream()
            .collect(
                Collectors.toMap(
                    ImportExcelDataDto::getDeviceNumber, dto -> getDeviceEntity(dto, farmMap),
                    (existing, replacement) -> replacement
                ));

        Set<String> deviceNumberSet = deviceMemoryMap.values()
            .stream()
            .map(Device::getDeviceNumber)
            .collect(Collectors.toSet());

        Page<Device> devicePage = deviceService.findAllByDeviceNumberIn(deviceNumberSet);
        List<DeviceDto> deviceDtoList = deviceDtoMapper.toDto(devicePage.getContent());
        Map<String, UUID> deviceMatchMap = deviceDtoList.stream()
            .collect(Collectors.toMap(DeviceDto::getDeviceNumber, DeviceDto::getId));

        List<Device> deviceListToSave = new ArrayList<>();

        for (Device device : deviceMemoryMap.values()) {
            if (deviceMatchMap.containsKey(device.getDeviceNumber())) {
                continue;
            }

            deviceListToSave.add(device);
        }

        if (deviceListToSave.isEmpty()) {
            return deviceMatchMap;
        }

        Set<String> deviceNumberSetToSave = deviceListToSave.stream()
            .map(Device::getDeviceNumber)
            .collect(Collectors.toSet());

        List<Device> deviceListSaved = deviceCreateManyCmd.withRequest(
                DeviceCreateManyCmd.Request.builder()
                    .deviceList(deviceListToSave)
                    .build())
            .execute();

        for (Device device : deviceListSaved) {
            deviceMatchMap.put(device.getDeviceNumber(), device.getId());
        }

        assignDeviceConnections(importExcelDataDtoBatch, deviceMatchMap, deviceNumberSetToSave);
        assignDeviceImeis(importExcelDataDtoBatch, deviceMatchMap, deviceNumberSetToSave);

        return deviceMatchMap;
    }

    private Device getDeviceEntity(ImportExcelDataDto dto, Map<String, UUID> farmMap) {
        var device = new Device();
        device.setName("Dispositivo-" + dto.getDeviceNumber());
        device.setDeviceNumber(dto.getDeviceNumber());
        device.setDeviceNumberLong(Long.valueOf(dto.getDeviceNumber()
            .trim()));
        device.setModel(Objects.requireNonNullElse(dto.getDeviceModel(), "N/A"));
        device.setBrand(Objects.requireNonNullElse(dto.getDeviceBrand(), "N/A"));
        device.setSerialNumber("N/A");
        device.setFarmId(farmMap.get(getFarmCode(dto.getFarmName())));

        return device;
    }

    private void assignDeviceConnections(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> deviceMap, Set<String> deviceNumberSetToSave)
    {
        Map<String, DeviceConnection> deviceConnectionMemoryMap = importExcelDataDtoBatch.stream()
            .filter(dto -> deviceNumberSetToSave.contains(dto.getDeviceNumber()))
            .collect(Collectors.toMap(
                ImportExcelDataDto::getMacAddress, dto -> getDeviceConnectionEntity(dto, deviceMap),
                (existing, replacement) -> replacement
            ));

        if (deviceConnectionMemoryMap.isEmpty()) {
            return;
        }

        deviceConnectionCreateManyCmd.withRequest(DeviceConnectionCreateManyCmd.Request.builder()
                .deviceConnectionList(deviceConnectionMemoryMap.values()
                    .stream()
                    .toList())
                .build())
            .execute();
    }

    private DeviceConnection getDeviceConnectionEntity(ImportExcelDataDto dto,
        Map<String, UUID> deviceMap)
    {
        var deviceConnection = new DeviceConnection();
        deviceConnection.setMacAddress(Objects.requireNonNullElse(dto.getMacAddress(), "N/A"));
        deviceConnection.setIpAddress(Objects.requireNonNullElse(dto.getIpAddress(), "N/A"));
        deviceConnection.setIpGateway(Objects.requireNonNullElse(dto.getIpGateway(), "N/A"));
        deviceConnection.setNetworkName("N/A");
        deviceConnection.setDeviceId(deviceMap.get(dto.getDeviceNumber()));

        return deviceConnection;
    }

    private void assignDeviceImeis(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> deviceMap, Set<String> deviceNumberSetToSave)
    {
        Map<String, DeviceImei> deviceImeiMemoryMap = importExcelDataDtoBatch.stream()
            .filter(dto -> deviceNumberSetToSave.contains(dto.getDeviceNumber()))
            .flatMap(dto -> Stream.of(dto.getImeiOne(), dto.getImeiTwo())
                .filter(Objects::nonNull)
                .map(imei -> Map.entry(imei, dto)))
            .collect(Collectors.toMap(
                Map.Entry::getKey, entry -> getDeviceImeiEntity(
                    entry.getKey(), entry.getValue()
                        .getDeviceNumber(), deviceMap
                ), (existing, replacement) -> existing
            ));

        if (deviceImeiMemoryMap.isEmpty()) {
            return;
        }

        deviceImeiCreateManyCmd.withRequest(DeviceImeiCreateManyCmd.Request.builder()
                .deviceImeiList(deviceImeiMemoryMap.values()
                    .stream()
                    .toList())
                .build())
            .execute();
    }

    private DeviceImei getDeviceImeiEntity(String imei, String deviceNumber,
        Map<String, UUID> deviceMap)
    {
        var deviceImei = new DeviceImei();
        deviceImei.setImei(imei);
        deviceImei.setDeviceId(deviceMap.get(deviceNumber));

        return deviceImei;
    }

    private Map<String, UUID> getFarmMap(List<ImportExcelDataDto> importExcelDataDtoBatch) {
        Map<String, Farm> farmMemoryMap = importExcelDataDtoBatch.stream()
            .collect(
                Collectors.toMap(
                    dto -> getFarmCode(dto.getFarmName()), this::getFarmEntity,
                    (existing, replacement) -> replacement
                ));

        Set<String> farmCodeSet = farmMemoryMap.values()
            .stream()
            .map(Farm::getCode)
            .collect(Collectors.toSet());

        Map<String, UUID> farmMatchMap = farmService.findAllByCodeIn(farmCodeSet)
            .getContent()
            .stream()
            .collect(Collectors.toMap(Farm::getCode, Farm::getId));

        List<Farm> farmListToSave = new ArrayList<>();

        for (Farm farm : farmMemoryMap.values()) {
            if (farmMatchMap.containsKey(farm.getCode())) {
                continue;
            }

            farmListToSave.add(farm);
        }

        if (!farmListToSave.isEmpty()) {
            List<Farm> farmListSaved = farmCreateManyCmd.withRequest(
                    FarmCreateManyCmd.Request.builder()
                        .farmList(farmListToSave)
                        .build())
                .execute();

            for (Farm farm : farmListSaved) {
                farmMatchMap.put(farm.getCode(), farm.getId());
            }
        }

        return farmMatchMap;
    }

    private String getFarmCode(String name) {
        return "FARM_" + name;
    }

    private Farm getFarmEntity(ImportExcelDataDto dto) {
        var farmEntity = new Farm();
        farmEntity.setName(dto.getFarmName());
        farmEntity.setCode(getFarmCode(dto.getFarmName()));
        farmEntity.setType(FarmTypeEnum.fromValue(dto.getFarmType()));

        return farmEntity;
    }

    @Builder
    public record Request(@NotNull List<ImportExcelDataDto> importExcelDataDtoList) {

    }
}
