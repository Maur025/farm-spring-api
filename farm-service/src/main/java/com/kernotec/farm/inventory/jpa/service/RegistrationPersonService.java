package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.repository.RegistrationPersonRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegistrationPersonService extends BaseServiceImpl<RegistrationPerson, UUID> {

    private final RegistrationPersonRepository repository;

    @Override
    protected String resourceName() {
        return "Registration Person";
    }

    @Override
    protected BaseRepository<RegistrationPerson, UUID> repository() {
        return repository;
    }

    public Optional<RegistrationPerson> findByPersonId(UUID personId) {
        return repository.findByPersonId(personId);
    }

    public Page<RegistrationPerson> findAllByPersonIdIn(Set<UUID> personIds) {
        Pageable pageable = PageableUtil.of(0, personIds.size() * 5, "id", false);
        return repository.findAllByPersonIdIn(personIds, pageable);
    }
}
