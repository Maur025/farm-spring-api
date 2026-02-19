package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.rest.dto.response.region.RegionMinRespone;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends BaseRepository<Region, UUID> {

    @Query("""
        SELECT r.id as id, r.name as name
        FROM Region r
        WHERE r.deleted = false
        AND (:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<RegionMinRespone> findAllMinData(@Param("keyword") String keyword, Pageable pageable);
}
