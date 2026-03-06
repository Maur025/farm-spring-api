package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.account.command.account.AccountCreateManyCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoFlatMapper;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.AccountService;
import com.kernotec.farm.parametric.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.AccountRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import com.kernotec.farm.util.LinkUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
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
public class HandleAccount implements HandleImport<AccountRequest> {

    private final AccountService accountService;

    private final AccountDtoFlatMapper accountDtoFlatMapper;

    private final CsvImportCommon csvImportCommon;
    private final AccountCreateManyCmd accountCreateManyCmd;
    private final LinkUtil linkUtil;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.ACCOUNT;
    }

    @Override
    public Map<String, UUID> handle(AccountRequest request) {
        Map<String, Account> accountMemoryMap = request.importExcelDataDtoBatch()
            .stream()
            .flatMap(dto -> getAccountStream(dto, request))
            .collect(Collectors.toMap(
                account -> csvImportCommon.getAccountUniqueCode(
                    request.inverseSocialNetworkMap()
                        .get(account.getSocialNetworkId()), account.getUsername(),
                    account.getAccountLink()
                ), account -> account
            ));

        Set<String> facebookAccountLinkSet = new HashSet<>();
        Set<String> accountUsernameSet = new HashSet<>();

        for (Account account : accountMemoryMap.values()) {
            String socialNetworkCodeString = request.inverseSocialNetworkMap()
                .get(account.getSocialNetworkId());

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
                accountDto -> csvImportCommon.getAccountUniqueCode(
                    request.inverseSocialNetworkMap()
                        .get(accountDto.getSocialNetworkId()), accountDto.getUsername(),
                    accountDto.getAccountLink()
                ), AccountDto::getId
            ));

        Page<Account> accountMatchPage = accountService.findAllByUsernameInAndType(
            accountUsernameSet, AccountTypeEnum.INTERNAL);
        List<AccountDto> accountMatchDtoList = accountDtoFlatMapper.toDto(
            accountMatchPage.getContent());

        Map<String, UUID> accountMatchMap = accountMatchDtoList.stream()
            .collect(Collectors.toMap(
                accountDto -> csvImportCommon.getAccountUniqueCode(
                    request.inverseSocialNetworkMap()
                        .get(accountDto.getSocialNetworkId()), accountDto.getUsername(),
                    accountDto.getAccountLink()
                ), AccountDto::getId
            ));

        List<Account> accountListToSave = getAccountListToSave(
            accountMemoryMap,
            facebookAccountMatchMap, accountMatchMap, request.inverseSocialNetworkMap()
        );

        if (!accountListToSave.isEmpty()) {
            List<Account> accountListSaved = accountCreateManyCmd.withRequest(
                    AccountCreateManyCmd.Request.builder()
                        .accountList(accountListToSave)
                        .build())
                .execute();

            for (Account account : accountListSaved) {
                String socialNetworkCodeString = request.inverseSocialNetworkMap()
                    .get(account.getSocialNetworkId());

                accountMatchMap.put(
                    csvImportCommon.getAccountUniqueCode(
                        socialNetworkCodeString, account.getUsername(), account.getAccountLink()),
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

    private Stream<Account> getAccountStream(ImportExcelDataDto dto, AccountRequest request) {
        List<SocialConfig> socialConfigs = getSocialConfigs();

        List<Account> accounts = new ArrayList<>();

        UUID personId = getPersonId(
            dto.getPersonFakeName(), dto.getPersonFakeLastName(), request.personMap());

        for (SocialConfig socialConfig : socialConfigs) {
            String username = socialConfig.userFn.apply(dto);
            String password = socialConfig.passwordFn.apply(dto);
            String link = socialConfig.linkFn.apply(dto);
            Boolean shouldBeCreated = socialConfig.shouldBeCreatedFn.apply(dto);

            if (!shouldBeCreated) {
                continue;
            }

            UUID socialNetworkId = request.socialNetworkMap()
                .get(socialConfig.type.toString());

            String identityUsername = getIdentityUsername(socialConfig.type, link);

            accounts.add(
                getAccountEntity(
                    username, password, link, identityUsername, socialNetworkId, personId));
        }

        return accounts.stream();
    }

    private UUID getPersonId(String name, String lastName, Map<String, UUID> personMap) {
        return personMap.get(csvImportCommon.getPersonCode(name, lastName));
    }

    private String getIdentityUsername(SocialNetworkEnum socialNetworkCode, String accountLink) {
        if (accountLink == null || accountLink.isBlank()) {
            return null;
        }

        return switch (socialNetworkCode) {
            case FACEBOOK -> linkUtil.getIdentityFacebookOfLink(accountLink);
            case TIKTOK -> {
                log.info("Tiktok account link:{}", accountLink);
                yield null;
            }
            default -> null;
        };
    }

    private Account getAccountEntity(String username, String password, String accountLink,
        String identityUsername, UUID socialNetworkId, UUID personId)
    {
        var account = new Account();

        account.setUsername(username);
        account.setPassword(password == null ? "N/A" : password);
        account.setPersonId(personId);
        account.setSocialNetworkId(socialNetworkId);
        account.setType(AccountTypeEnum.INTERNAL);
        account.setIsEnabled(true);
        account.setAccountLink(accountLink);
        account.setIdentityUsername(identityUsername);

        return account;
    }

    private List<Account> getAccountListToSave(Map<String, Account> accountMemoryMap,
        Map<String, UUID> facebookAccountMatchMap, Map<String, UUID> accountMatchMap,
        Map<UUID, String> inverseSocialNetworkMap)
    {
        List<Account> accountListToSave = new ArrayList<>();

        for (Account account : accountMemoryMap.values()) {
            SocialNetworkEnum socialNetworkCode = SocialNetworkEnum.fromValue(
                inverseSocialNetworkMap.get(account.getSocialNetworkId()));

            if (SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && facebookAccountMatchMap.containsKey(
                csvImportCommon.getAccountUniqueCode(
                    socialNetworkCode.toString(),
                    account.getUsername(), account.getAccountLink()
                )))
            {
                continue;
            }

            if (!SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && accountMatchMap.containsKey(
                csvImportCommon.getAccountUniqueCode(
                    socialNetworkCode.toString(),
                    account.getUsername(), account.getAccountLink()
                )))
            {
                continue;
            }

            accountListToSave.add(account);
        }

        return accountListToSave;
    }

    private List<SocialConfig> getSocialConfigs() {
        return List.of(
            SocialConfig.builder()
                .type(SocialNetworkEnum.FACEBOOK)
                .userFn(ImportExcelDataDto::getFacebookUsername)
                .passwordFn(ImportExcelDataDto::getFacebookPassword)
                .linkFn(ImportExcelDataDto::getFacebookAccountLink)
                .shouldBeCreatedFn(dto -> shouldBeCreated(dto.getFacebookUsername()))
                .build(),

            SocialConfig.builder()
                .type(SocialNetworkEnum.TIKTOK)
                .userFn(ImportExcelDataDto::getTiktokUsername)
                .passwordFn(ImportExcelDataDto::getTiktokPassword)
                .linkFn(dto -> null)
                .shouldBeCreatedFn(dto -> shouldBeCreated(dto.getTiktokUsername()))
                .build(),

            SocialConfig.builder()
                .type(SocialNetworkEnum.X)
                .userFn(ImportExcelDataDto::getXUsername)
                .passwordFn(ImportExcelDataDto::getXPassword)
                .linkFn(dto -> null)
                .shouldBeCreatedFn(dto -> shouldBeCreated(dto.getXUsername()))
                .build(),

            SocialConfig.builder()
                .type(SocialNetworkEnum.CORREO)
                .userFn(ImportExcelDataDto::getEmailUsername)
                .passwordFn(ImportExcelDataDto::getEmailPassword)
                .linkFn(dto -> null)
                .shouldBeCreatedFn(dto -> shouldBeCreated(dto.getEmailUsername()))
                .build(),

            SocialConfig.builder()
                .type(SocialNetworkEnum.WHATSAPP)
                .userFn(ImportExcelDataDto::getPhoneNumber)
                .passwordFn(dto -> "N/A")
                .linkFn(dto -> null)
                .shouldBeCreatedFn(ImportExcelDataDto::getIsHaveWhatsapp)
                .build()
        );
    }

    private Boolean shouldBeCreated(String username) {
        return username != null && !username.isBlank() && !username.equalsIgnoreCase("N/A");
    }

    @Builder
    public record SocialConfig(SocialNetworkEnum type, Function<ImportExcelDataDto, String> userFn,
                               Function<ImportExcelDataDto, String> passwordFn,
                               Function<ImportExcelDataDto, String> linkFn,
                               Function<ImportExcelDataDto, Boolean> shouldBeCreatedFn)
    {

    }
}
