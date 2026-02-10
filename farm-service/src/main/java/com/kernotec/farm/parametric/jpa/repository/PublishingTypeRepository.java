package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import com.kernotec.farm.parametric.rest.dto.response.publishing.type.PublishingTypeMinResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingTypeRepository extends BaseRepository<PublishingType, UUID> {

    @Query("""
        SELECT pt.id as id, pt.name as name
        FROM PublishingType pt
        WHERE pt.deleted = false
        AND (:keyword IS NULL OR LOWER(pt.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<PublishingTypeMinResponse> findAllMinData(@Param("keyword") String keyword,
        Pageable pageable);
}
