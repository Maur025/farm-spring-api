package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService extends BaseServiceImpl<Person, UUID> {

    private final PersonRepository repository;

    @Override
    protected String resourceName() {
        return "Person";
    }

    @Override
    protected BaseRepository<Person, UUID> repository() {
        return repository;
    }

    public Optional<Person> findByNameAndLastName(String name, String lastName) {
        return repository.findByNameAndLastName(name, lastName);
    }

    public Page<Person> findAllByNameAndLastNameInList(List<String> nameAndLastNames)
    {
        Pageable pageable = PageableUtil.of(0, nameAndLastNames.size(), "name", false);
        return repository.findAllByNameAndLastNameInList(nameAndLastNames, pageable);
    }
}
