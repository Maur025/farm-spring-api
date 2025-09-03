package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends BaseRepository<Operator, UUID> {

    Optional<Operator> findByCode(OperatorCodeEnum code);
}
