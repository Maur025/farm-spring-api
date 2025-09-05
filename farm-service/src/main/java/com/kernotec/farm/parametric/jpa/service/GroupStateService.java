package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.exception.GroupStateException;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.parametric.jpa.repository.GroupStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GroupStateService extends BaseServiceImpl<GroupState, UUID> {

    private final GroupStateRepository repository;

    @Override
    protected String resourceName() {
        return "Group State";
    }

    @Override
    protected BaseRepository<GroupState, UUID> repository() {
        return repository;
    }

    public Optional<GroupState> findByCode(GroupStateCodeEnum code) {
        return repository.findByCode(code.toString());
    }

    public GroupState findByCodeThrow(GroupStateCodeEnum code) {
        return findByCode(code).orElseThrow(
            () -> new GroupStateException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }
}
