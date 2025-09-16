package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountObservation;
import com.kernotec.farm.account.jpa.repository.AccountObservationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountObservationService extends BaseServiceImpl<AccountObservation, UUID> {

    private final AccountObservationRepository repository;

    @Override
    protected String resourceName() {
        return "Account Observation";
    }

    @Override
    protected BaseRepository<AccountObservation, UUID> repository() {
        return repository;
    }
}
