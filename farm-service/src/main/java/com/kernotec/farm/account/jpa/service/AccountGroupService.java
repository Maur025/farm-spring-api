package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.repository.AccountGroupRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountGroupService extends BaseServiceImpl<AccountGroup, UUID> {

    private final AccountGroupRepository repository;

    @Override
    protected String resourceName() {
        return "Account Group";
    }

    @Override
    protected BaseRepository<AccountGroup, UUID> repository() {
        return repository;
    }
}
