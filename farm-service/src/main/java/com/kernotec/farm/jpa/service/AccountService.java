package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Account;
import com.kernotec.farm.jpa.entity.Chip;
import com.kernotec.farm.jpa.entity.Device;
import com.kernotec.farm.jpa.entity.Farm;
import com.kernotec.farm.jpa.entity.Person;
import com.kernotec.farm.jpa.repository.AccountRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountService extends BaseServiceImpl<Account, UUID> {

    private final AccountRepository repository;

    @Override
    protected String resourceName() {
        return "Account";
    }

    @Override
    protected BaseRepository<Account, UUID> repository() {
        return repository;
    }

    public Page<Account> findAllWithFilters(UUID socialNetworkId, String keyword, Pageable pageable)
    {
        String pattern = keyword == null || keyword.isBlank() ? null : keyword.toLowerCase() + "%";

        return repository.findAll(
            (Specification<Account>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                if (socialNetworkId != null) {
                    predicateList.add(cb.equal(root.get("socialNetworkId"), socialNetworkId));
                }

                if (pattern != null) {
                    Join<Account, Person> personJoin = root.join("person", JoinType.INNER);
                    Join<Account, Chip> chipJoin = root.join("chips", JoinType.INNER);
                    Join<Account, Device> deviceJoin = root.join("devices", JoinType.INNER);
                    Join<Farm, Device> farmDeviceJoin = deviceJoin.join("farm", JoinType.INNER);

                    predicateList.add(cb.or(
                        cb.like(cb.lower(root.get("username")), pattern),
                        cb.like(cb.lower(chipJoin.get("phoneNumber")), pattern),
                        cb.like(cb.lower(personJoin.get("name")), pattern),
                        cb.like(cb.lower(personJoin.get("lastName")), pattern),
                        cb.like(cb.lower(deviceJoin.get("deviceNumber")), pattern),
                        cb.like(cb.lower(farmDeviceJoin.get("name")), pattern)
                    ));
                }

                query.distinct(true);
                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }

    public Page<Account> findByUsername(String username, Pageable pageable) {
        String usernamePattern =
            username == null || username.isBlank() ? null : username.toLowerCase() + "%";

        return repository.findAll(
            (Specification<Account>) (root, query, cb) -> {
                if (usernamePattern == null) {
                    return cb.conjunction();
                }

                return cb.and(cb.like(cb.lower(root.get("username")), usernamePattern));
            }, pageable
        );
    }
}
