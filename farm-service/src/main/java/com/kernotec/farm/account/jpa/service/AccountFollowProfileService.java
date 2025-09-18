package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.account.jpa.repository.AccountFollowProfileRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountFollowProfileService extends BaseServiceImpl<AccountFollowProfile, UUID> {

    private final AccountFollowProfileRepository repository;

    @Override
    protected String resourceName() {
        return "Account Follow Profile";
    }

    @Override
    protected BaseRepository<AccountFollowProfile, UUID> repository() {
        return repository;
    }

    public Optional<AccountFollowProfile> findByAccountIdAndProfileIdAndFollowState(UUID accountId,
        UUID profileId, AccountFollowProfileStateEnum followState)
    {
        return repository.findByAccountIdAndProfileIdAndFollowState(
            accountId, profileId, followState);
    }

    public AccountFollowProfile findByAccountIdAndProfileIdAndFollowStateThrow(UUID accountId,
        UUID profileId, AccountFollowProfileStateEnum followState)
    {
        return findByAccountIdAndProfileIdAndFollowState(
            accountId, profileId, followState).orElseThrow(
            () -> new IllegalArgumentException(String.format(
                "%s with accountId %s, profileId %s and followState %s not found", resourceName(),
                accountId, profileId, followState
            )));
    }
}
