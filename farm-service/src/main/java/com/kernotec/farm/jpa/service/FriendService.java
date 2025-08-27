package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Friend;
import com.kernotec.farm.jpa.repository.FriendRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FriendService extends BaseServiceImpl<Friend, UUID> {

    private final FriendRepository repository;

    @Override
    protected String resourceName() {
        return "Friend";
    }

    @Override
    protected BaseRepository<Friend, UUID> repository() {
        return repository;
    }
}
