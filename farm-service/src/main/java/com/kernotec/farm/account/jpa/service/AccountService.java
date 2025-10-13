package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.repository.AccountRepository;
import com.kernotec.farm.account.jpa.specification.account.AccountSpecification;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountService extends BaseServiceImpl<Account, UUID> {

    private final AccountRepository repository;

    @Override
    protected String resourceName() {
        return "Account";
    }

    @Override
    protected BaseRepository<Account, UUID> repository() {
        return repository;
    }

    public Page<Account> findAllInternalWithFilters(UUID socialNetworkId, String keyword,
        Pageable pageable)
    {
        return repository.findAll(
            AccountSpecification.builder()
                .withSocialNetworkId(socialNetworkId)
                .withKeyword(keyword)
                .withAccountType(AccountTypeEnum.INTERNAL), pageable
        );
    }

    public Page<Account> searchByUsernameOrLink(String username, UUID socialNetworkId,
        UUID ignoreAccountId, String link, Pageable pageable)
    {

        return repository.findAll(
            AccountSpecification.builder()
                .withUsernameSearch(username)
                .withSocialNetworkId(socialNetworkId)
                .ignoreAccountId(ignoreAccountId)
                .withLinkSearch(link), pageable
        );
    }

    public Page<Account> findAllWithMinData(UUID socialNetworkId, Pageable pageable) {
        return repository.findAll(
            AccountSpecification.builder()
                .withSocialNetworkId(socialNetworkId)
                .withAccountType(AccountTypeEnum.INTERNAL), pageable
        );
    }

    public Optional<Account> findByAccountLinkAndSocialNetworkId(String accountLink,
        UUID socialNetworkId)
    {
        return repository.findByAccountLinkAndSocialNetworkId(accountLink, socialNetworkId);
    }
}
