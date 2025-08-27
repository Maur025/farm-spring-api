package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Account;
import com.kernotec.farm.jpa.repository.AccountRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountService extends BaseServiceImpl<Account, UUID> {

    private final AccountRepository repository;

    @Override
    protected String resourceName() {
        return "Account";
    }

    @Override
    protected BaseRepository<Account, UUID> repository() {
        return repository;
    }
}
