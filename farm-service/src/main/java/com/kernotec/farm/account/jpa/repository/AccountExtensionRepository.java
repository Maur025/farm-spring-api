package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountExtensionRepository extends BaseRepository<AccountExtension, UUID> {

}
