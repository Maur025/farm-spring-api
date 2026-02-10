package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmMinResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends BaseRepository<Farm, UUID> {

    Optional<Farm> findByCode(String code);

    @Query("""
        SELECT f.id as id, f.name as name
        FROM Farm f
        WHERE f.deleted = false
        AND (:keyword IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<FarmMinResponse> findAllMinData(@Param("keyword") String keyword, Pageable pageable);
}
