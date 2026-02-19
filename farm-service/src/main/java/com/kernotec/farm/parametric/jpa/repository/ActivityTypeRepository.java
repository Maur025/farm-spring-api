package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeMinResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityTypeRepository extends BaseRepository<ActivityType, UUID> {

    Optional<ActivityType> findByCode(String code);

    @Query("""
        SELECT at.id as id, at.name as name
        FROM ActivityType at
        INNER JOIN SocialNetworkAction sna ON sna.activityTypeId = at.id
        WHERE at.deleted = false
        AND (CAST(:socialNetworkId AS uuid) IS NULL OR sna.socialNetworkId = :socialNetworkId )
        AND (:keyword IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        GROUP BY at.id, at.name
        """)
    Page<ActivityTypeMinResponse> findAllMinData(@Param("socialNetworkId") UUID socialNetworkId,
        @Param("keyword") String keyword, Pageable pageable);
}
