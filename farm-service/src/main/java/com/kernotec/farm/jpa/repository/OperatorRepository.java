package com.kernotec.farm.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.jpa.entity.Operator;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends BaseRepository<Operator, UUID> {

    Optional<Operator> findByCode(OperatorCodeEnum code);
}
