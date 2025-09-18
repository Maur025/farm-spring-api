package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.repository.AccountFollowProfileRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountFollowProfileService extends BaseServiceImpl<AccountFollowProfile, UUID> {

    private final AccountFollowProfileRepository repository;

    @Override
    protected String resourceName() {
        return "Account Follow Profile";
    }

    @Override
    protected BaseRepository<AccountFollowProfile, UUID> repository() {
        return repository;
    }
}
