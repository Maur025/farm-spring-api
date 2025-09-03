package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.DeviceAccount;
import com.kernotec.farm.account.jpa.repository.DeviceAccountRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceAccountService extends BaseServiceImpl<DeviceAccount, UUID> {

    private final DeviceAccountRepository repository;

    @Override
    protected String resourceName() {
        return "Device Account";
    }

    @Override
    protected BaseRepository<DeviceAccount, UUID> repository() {
        return repository;
    }
}
