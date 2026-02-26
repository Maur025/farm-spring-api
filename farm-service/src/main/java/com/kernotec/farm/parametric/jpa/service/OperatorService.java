package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.parametric.exception.OperatorException;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.parametric.jpa.repository.OperatorRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Operator> findAllByCodeIn(Set<OperatorCodeEnum> codeSet) {
        Pageable pageable = PageableUtil.of(0, codeSet.size(), "code", false);
        return repository.findAllByCodeIn(codeSet, pageable);
    }
}
