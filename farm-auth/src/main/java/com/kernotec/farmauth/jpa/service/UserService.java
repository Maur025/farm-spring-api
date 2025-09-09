package com.kernotec.farmauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.repository.UserRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService extends BaseServiceImpl<User, UUID> {

    private final UserRepository repository;

    @Override
    protected String resourceName() {
        return "User";
    }

    @Override
    protected BaseRepository<User, UUID> repository() {
        return repository;
    }
}
