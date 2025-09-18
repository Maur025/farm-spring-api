package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountFollowProfileRepository extends BaseRepository<AccountFollowProfile, UUID> {

    Optional<AccountFollowProfile> findByAccountIdAndProfileIdAndFollowState(UUID accountId,
        UUID profileId, AccountFollowProfileStateEnum followState);
}
