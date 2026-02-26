package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.repository.AccountRepository;
import com.kernotec.farm.account.jpa.specification.account.AccountSpecification;
import com.kernotec.farm.account.rest.dto.response.account.AccountMinResponse;
import java.util.Optional;
import java.util.Set;
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

    public Page<AccountMinResponse> findAllWithMinData(UUID socialNetworkId, String keyword,
        Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repository.findAllMinData(socialNetworkId, keywordStr, pageable);
    }

    public Optional<Account> findByAccountLinkAndSocialNetworkId(String accountLink,
        UUID socialNetworkId)
    {
        return repository.findByAccountLinkAndSocialNetworkId(accountLink, socialNetworkId);
    }

    public Page<Account> findAllByUsernameInAndType(Set<String> usernameSet, AccountTypeEnum type) {
        Pageable pageable = PageableUtil.of(0, usernameSet.size(), "username", false);
        return repository.findAllByUsernameInAndType(usernameSet, type, pageable);
    }

    public Page<Account> findAllByAccountLinkIn(Set<String> accountLinkSet) {
        Pageable pageable = PageableUtil.of(0, accountLinkSet.size() * 2, "accountLink", false);
        return repository.findAllByAccountLinkIn(accountLinkSet, pageable);
    }
}
