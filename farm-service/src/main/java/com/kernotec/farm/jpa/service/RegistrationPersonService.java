package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.RegistrationPerson;
import com.kernotec.farm.jpa.repository.RegistrationPersonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
}
