package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountExtensionLog;
import com.kernotec.farm.account.jpa.repository.AccountExtensionLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountExtensionLogService extends BaseServiceImpl<AccountExtensionLog, UUID> {

    private final AccountExtensionLogRepository repository;

    @Override
    protected String resourceName() {
        return "Account Extension Log";
    }

    @Override
    protected BaseRepository<AccountExtensionLog, UUID> repository() {
        return repository;
    }
}
