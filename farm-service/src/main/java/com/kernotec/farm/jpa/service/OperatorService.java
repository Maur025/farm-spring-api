package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.exception.OperatorException;
import com.kernotec.farm.jpa.entity.Operator;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.jpa.repository.OperatorRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OperatorService extends BaseServiceImpl<Operator, UUID> {

    private final OperatorRepository repository;

    @Override
    protected String resourceName() {
        return "Operator";
    }

    @Override
    protected BaseRepository<Operator, UUID> repository() {
        return repository;
    }

    public Optional<Operator> findByCode(OperatorCodeEnum code) {
        return repository.findByCode(code);
    }

    public Operator findByCodeThrow(OperatorCodeEnum code) {
        return findByCode(code).orElseThrow(
            () -> new OperatorException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }
}
