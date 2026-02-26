package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationPersonRepository extends BaseRepository<RegistrationPerson, UUID> {

    Optional<RegistrationPerson> findByPersonId(UUID personId);

    Page<RegistrationPerson> findAllByPersonIdIn(Set<UUID> personIds, Pageable pageable);
}
