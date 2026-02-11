package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.jpa.repository.RegionRepository;
import com.kernotec.farm.parametric.rest.dto.response.region.RegionMinRespone;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegionService extends BaseServiceImpl<Region, UUID> {

    private final RegionRepository repository;

    @Override
    protected String resourceName() {
        return "Region";
    }

    @Override
    protected BaseRepository<Region, UUID> repository() {
        return repository;
    }

    public Page<RegionMinRespone> findAllMinData(@Param("keyword") String keyword,
        Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repository.findAllMinData(keywordStr, pageable);
    }
}
