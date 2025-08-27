package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Group;
import com.kernotec.farm.jpa.repository.GroupRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GroupService extends BaseServiceImpl<Group, UUID> {

    private final GroupRepository repository;

    @Override
    protected String resourceName() {
        return "Group";
    }

    @Override
    protected BaseRepository<Group, UUID> repository() {
        return repository;
    }
}
