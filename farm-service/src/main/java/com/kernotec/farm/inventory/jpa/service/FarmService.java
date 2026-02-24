package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.repository.FarmRepository;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmMinResponse;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FarmService extends BaseServiceImpl<Farm, UUID> {

    private final FarmRepository repository;

    @Override
    protected String resourceName() {
        return "Farm";
    }

    @Override
    protected BaseRepository<Farm, UUID> repository() {
        return repository;
    }

    public Optional<Farm> findByCode(String code) {
        return repository.findByCode(code);
    }

    public Page<FarmMinResponse> findAllMinData(String keyword, Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return repository.findAllMinData(keywordStr, pageable);
    }

    public Page<Farm> findAllByCodeIn(Set<String> codes) {
        Pageable pageable = PageableUtil.of(0, codes.size(), "code", false);

        return repository.findAllByCodeIn(codes, pageable);
    }
}
