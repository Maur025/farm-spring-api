package com.kernotec.farm.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.jpa.entity.Friend;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends BaseRepository<Friend, UUID> {

}
