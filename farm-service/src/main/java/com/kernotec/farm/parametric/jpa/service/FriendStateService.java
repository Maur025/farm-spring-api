package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.exception.FriendStateException;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.repository.FriendStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FriendStateService extends BaseServiceImpl<FriendState, UUID> {

    private final FriendStateRepository repository;

    @Override
    protected String resourceName() {
        return "Friend State";
    }

    @Override
    protected BaseRepository<FriendState, UUID> repository() {
        return repository;
    }

    public Optional<FriendState> findByCode(FriendStateCodeEnum code) {
        return repository.findByCode(code.toString());
    }

    public FriendState findByCodeThrow(FriendStateCodeEnum code) {
        return findByCode(code).orElseThrow(
            () -> new FriendStateException(
                "code.not.found", "'" + code + "'",
                HttpStatus.NOT_FOUND.value()
            ));
    }
}
