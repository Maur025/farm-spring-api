package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.exception.SocialNetworkException;
import com.kernotec.farm.jpa.entity.SocialNetwork;
import com.kernotec.farm.jpa.repository.SocialNetworkRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SocialNetworkService extends BaseServiceImpl<SocialNetwork, UUID> {

    private final SocialNetworkRepository repository;

    @Override
    protected String resourceName() {
        return "Social Network";
    }

    @Override
    protected BaseRepository<SocialNetwork, UUID> repository() {
        return repository;
    }

    public Optional<SocialNetwork> findByCode(String code) {
        return repository.findByCode(code);
    }

    public SocialNetwork findByCodeThrow(String code) {
        return findByCode(code).orElseThrow(
            () -> new SocialNetworkException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }
}
