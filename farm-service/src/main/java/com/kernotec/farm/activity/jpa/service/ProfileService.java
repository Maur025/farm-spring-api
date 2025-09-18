package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.activity.jpa.repository.ProfileRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService extends BaseServiceImpl<Profile, UUID> {

    private final ProfileRepository repository;

    @Override
    protected String resourceName() {
        return "Profile";
    }

    @Override
    protected BaseRepository<Profile, UUID> repository() {
        return repository;
    }
}
