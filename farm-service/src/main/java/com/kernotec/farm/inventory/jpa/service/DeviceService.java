package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.repository.DeviceRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceService extends BaseServiceImpl<Device, UUID> {

    private final DeviceRepository repository;

    @Override
    protected String resourceName() {
        return "Device";
    }

    @Override
    protected BaseRepository<Device, UUID> repository() {
        return repository;
    }

    public Page<Device> findAllWithFilters(String keyword, UUID socialNetworkId, Pageable pageable)
    {
        String pattern = keyword == null || keyword.isBlank() ? null : keyword.toLowerCase() + "%";

        return repository.findAll(
            (Specification<Device>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                Join<Device, Account> accountJoin = root.join("accounts", JoinType.INNER);

                if (socialNetworkId != null) {

                    predicateList.add(
                        cb.equal(accountJoin.get("socialNetworkId"), socialNetworkId));
                }

                if (pattern != null) {
                    Join<Account, Person> personJoin = accountJoin.join("person", JoinType.INNER);
                    predicateList.add(cb.or(
                        cb.equal(cb.lower(root.get("deviceNumber")), keyword),
                        cb.like(cb.lower(accountJoin.get("username")), pattern),
                        cb.like(cb.lower(personJoin.get("name")), pattern),
                        cb.like(cb.lower(personJoin.get("lastName")), pattern)
                    ));
                }

                query.distinct(true);
                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }

    public Optional<Device> findByDeviceNumber(String deviceNumber) {
        return repository.findByDeviceNumber(deviceNumber);
    }
}
