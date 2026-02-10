package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.parametric.rest.dto.response.publishing.context.PublishingContextMinResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingContextRepository extends BaseRepository<PublishingContext, UUID> {

    @Query("""
        SELECT pc.id as id, pc.name as name
        FROM PublishingContext pc
        WHERE pc.deleted = false
        AND (:keyword IS NULL OR LOWER(pc.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<PublishingContextMinResponse> findAllMinData(@Param("keyword") String keyword,
        Pageable pageable);
}
