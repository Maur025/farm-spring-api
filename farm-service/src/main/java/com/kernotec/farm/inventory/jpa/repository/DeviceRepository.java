package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceMinResponse;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends BaseRepository<Device, UUID> {

    Optional<Device> findByDeviceNumber(String deviceNumber);

    @Query("""
        SELECT d.id as id, d.deviceNumber as deviceNumber
        FROM Device d
        WHERE d.deleted = false
        AND (CAST(:farmId as uuid) IS NULL OR d.farmId = :farmId)
        AND (:keyword IS NULL OR LOWER(d.deviceNumber) LIKE LOWER(CONCAT('%',:keyword,'%')))
        """)
    Page<DeviceMinResponse> findAllMinData(@Param("farmId") UUID farmId,
        @Param("keyword") String keyword, Pageable pageable);

    Page<Device> findAllByDeviceNumberIn(Set<String> deviceNumbers, Pageable pageable);
}
