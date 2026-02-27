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
import java.util.stream.Collectors;
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
            .flatMap(dto -> {
                List<Account> accounts = new ArrayList<>();

                if (dto.getFacebookUsername() != null) {
                    UUID socialNetworkId = request.socialNetworkMap()
                        .get(SocialNetworkEnum.FACEBOOK.toString());

                    Account facebookAccount = getAccountEntity(
                        dto.getFacebookUsername(), dto.getFacebookPassword(), socialNetworkId,
                        dto.getFacebookAccountLink(), dto, request.personMap(),
                        request.inverseSocialNetworkMap()
                    );

                    accounts.add(facebookAccount);
                }

                if (dto.getTiktokUsername() != null) {
                    UUID socialNetworkId = request.socialNetworkMap()
                        .get(SocialNetworkEnum.TIKTOK.toString());

                    Account tiktokAccount = getAccountEntity(
                        dto.getTiktokUsername(), dto.getTiktokPassword(), socialNetworkId, dto,
                        request.personMap()
                    );

                    accounts.add(tiktokAccount);
                }

                if (dto.getXUsername() != null) {
                    UUID socialNetworkId = request.socialNetworkMap()
                        .get(SocialNetworkEnum.X.toString());

                    Account xAccount = getAccountEntity(
                        dto.getXUsername(), dto.getXPassword(),
                        socialNetworkId, dto, request.personMap()
                    );

                    accounts.add(xAccount);
                }

                if (dto.getIsHaveWhatsapp() && dto.getPhoneNumber() != null && !dto.getPhoneNumber()
                    .equalsIgnoreCase("N/A"))
                {
                    UUID socialNetworkId = request.socialNetworkMap()
                        .get(SocialNetworkEnum.WHATSAPP.toString());

                    Account whatsappAccount = getAccountEntity(
                        dto.getPhoneNumber(), null, socialNetworkId, dto, request.personMap());

                    accounts.add(whatsappAccount);
                }

                if (dto.getEmailUsername() != null) {
                    UUID socialNetworkId = request.socialNetworkMap()
                        .get(SocialNetworkEnum.CORREO.toString());

                    Account emailAccount = getAccountEntity(
                        dto.getEmailUsername(), dto.getEmailPassword(), socialNetworkId, dto,
                        request.personMap()
                    );

                    accounts.add(emailAccount);
                }

                return accounts.stream();
            })
            .collect(Collectors.toMap(
                account -> csvImportCommon.getAccountUniqueCode(
                    request.inverseSocialNetworkMap()
                        .get(account.getSocialNetworkId()), account.getUsername()
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
                        .get(accountDto.getSocialNetworkId()), accountDto.getUsername()
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
                        .get(accountDto.getSocialNetworkId()), accountDto.getUsername()
                ), AccountDto::getId
            ));

        List<Account> accountListToSave = new ArrayList<>();

        for (Account account : accountMemoryMap.values()) {
            SocialNetworkEnum socialNetworkCode = SocialNetworkEnum.fromValue(
                request.inverseSocialNetworkMap()
                    .get(account.getSocialNetworkId()));

            if (SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && facebookAccountMatchMap.containsKey(
                csvImportCommon.getAccountUniqueCode(
                    socialNetworkCode.toString(),
                    account.getUsername()
                )))
            {
                continue;
            }

            if (!SocialNetworkEnum.FACEBOOK.equals(socialNetworkCode)
                && accountMatchMap.containsKey(
                csvImportCommon.getAccountUniqueCode(
                    socialNetworkCode.toString(),
                    account.getUsername()
                )))
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
                String socialNetworkCodeString = request.inverseSocialNetworkMap()
                    .get(account.getSocialNetworkId());

                accountMatchMap.put(
                    csvImportCommon.getAccountUniqueCode(
                        socialNetworkCodeString, account.getUsername()), account.getId()
                );
            }
        }

        facebookAccountMatchMap.forEach((key, value) -> accountMatchMap.merge(
            key, value,
            (existing, replacement) -> replacement
        ));

        return accountMatchMap;
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
        account.setPersonId(personMap.get(
            csvImportCommon.getPersonCode(dto.getPersonFakeName(), dto.getPersonFakeLastName())));
        account.setSocialNetworkId(socialNetworkId);
        account.setType(AccountTypeEnum.INTERNAL);
        account.setIsEnabled(true);
        account.setAccountLink(accountLink);
        account.setIdentityUsername(identityUsername);

        return account;
    }
}
