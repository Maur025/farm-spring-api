package com.kernotec.farmauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farmauth.jpa.entity.User;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

}
