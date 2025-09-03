package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStateRepository extends BaseRepository<RequestState, UUID> {

}
