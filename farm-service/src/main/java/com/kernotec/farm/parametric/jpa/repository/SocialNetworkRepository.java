package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkMinResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialNetworkRepository extends BaseRepository<SocialNetwork, UUID> {

    Optional<SocialNetwork> findByCode(String code);

    @Query("""
        SELECT sn.id as id, sn.name as name
        FROM SocialNetwork sn
        WHERE sn.deleted = false
        AND ((:isHasAccounts IS NULL OR :isHasAccounts = false) OR EXISTS (SELECT 1 FROM Account a WHERE a.socialNetworkId = sn.id))
        AND ((:isHasActivityTypes IS NULL OR :isHasActivityTypes = false) OR EXISTS (SELECT 1 FROM SocialNetworkAction sna WHERE sna.socialNetworkId = sn.id))
        AND (:keyword IS NULL OR LOWER(sn.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<SocialNetworkMinResponse> findAllMinData(@Param("isHasAccounts") Boolean isHasAccounts,
        @Param("isHasActivityTypes") Boolean isHasActivityTypes, @Param("keyword") String keyword,
        Pageable pageable);
}
