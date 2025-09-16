package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountGroupRepository extends BaseRepository<AccountGroup, UUID> {

    Optional<AccountGroup> findByAccountIdAndGroupIdAndGroupStateId(UUID accountId, UUID groupId,
        UUID groupStateId);
}
