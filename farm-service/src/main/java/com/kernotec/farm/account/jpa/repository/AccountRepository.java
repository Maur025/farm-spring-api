package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.dto.response.account.AccountMinResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<Account, UUID> {

    Optional<Account> findByAccountLinkAndSocialNetworkId(String accountLink, UUID socialNetworkId);

    @Query("""
        SELECT a.id as id, a.username as username, a.accountLink as accountLink
        FROM Account a
        WHERE a.deleted = false
        AND (CAST(:socialNetworkId AS uuid) IS NULL OR a.socialNetworkId = :socialNetworkId)
        AND (:keyword IS NULL OR LOWER(a.username) LIKE LOWER(CONCAT('%',:keyword,'%')))
        AND a.type = 'INTERNAL'
        """)
    Page<AccountMinResponse> findAllMinData(@Param("socialNetworkId") UUID socialNetworkId,
        @Param("keyword") String keyword, Pageable pageable);
}
