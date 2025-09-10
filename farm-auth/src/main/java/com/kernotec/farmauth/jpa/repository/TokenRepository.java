package com.kernotec.farmauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farmauth.jpa.entity.Token;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends BaseRepository<Token, UUID> {

}
