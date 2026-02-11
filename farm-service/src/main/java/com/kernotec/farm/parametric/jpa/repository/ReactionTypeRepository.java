package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.parametric.rest.dto.response.reaction.type.ReactionTypeMinResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionTypeRepository extends BaseRepository<ReactionType, UUID> {

    @Query("""
        SELECT rt.id as id, rt.name as name
        FROM ReactionType rt
        WHERE rt.deleted = false
        AND (CAST(:socialNetworkId AS uuid) IS NULL OR rt.socialNetworkId = :socialNetworkId)
        AND (:keyword IS NULL OR LOWER(rt.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<ReactionTypeMinResponse> findAllMinData(@Param("socialNetworkId") UUID socialNetworkId,
        @Param("keyword") String keyword, Pageable pageable);
}
