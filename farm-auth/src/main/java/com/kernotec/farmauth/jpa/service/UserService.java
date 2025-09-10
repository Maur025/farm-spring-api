package com.kernotec.farmauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByUsernameThrow(String username) {
        return findByUsername(username).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username or Password invalid"));
    }
}
