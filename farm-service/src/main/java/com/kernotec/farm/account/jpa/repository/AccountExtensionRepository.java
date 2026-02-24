package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountExtensionRepository extends BaseRepository<AccountExtension, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        DELETE FROM AccountExtension ae
        WHERE ae.accountId = :accountId
        """)
    void deleteAllByAccountId(@Param("accountId") UUID accountId);
}
