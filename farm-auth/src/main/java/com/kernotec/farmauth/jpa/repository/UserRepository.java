package com.kernotec.farmauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farmauth.jpa.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
