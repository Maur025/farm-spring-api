package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.parametric.jpa.repository.PublishingContextRepository;
import com.kernotec.farm.parametric.rest.dto.response.publishing.context.PublishingContextMinResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PublishingContextService extends BaseServiceImpl<PublishingContext, UUID> {

    private final PublishingContextRepository repository;

    @Override
    protected String resourceName() {
        return "Publishing Context";
    }

    @Override
    protected BaseRepository<PublishingContext, UUID> repository() {
        return repository;
    }

    public Page<PublishingContextMinResponse> findAllMinData(String keyword, Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repository.findAllMinData(keywordStr, pageable);
    }
}
