package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import com.kernotec.farm.account.jpa.repository.AccountExtensionRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountExtensionService extends BaseServiceImpl<AccountExtension, UUID> {

    private final AccountExtensionRepository repository;

    @Override
    protected String resourceName() {
        return "Account Extension";
    }

    @Override
    protected BaseRepository<AccountExtension, UUID> repository() {
        return repository;
    }
}
