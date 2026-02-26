package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.Person;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends BaseRepository<Person, UUID> {

    Optional<Person> findByNameAndLastName(String name, String lastName);

    @Query("""
        SELECT p
        FROM Person p
        WHERE CONCAT(p.name, '|', p.lastName) IN :nameAndLastNames
        """)
    Page<Person> findAllByNameAndLastNameInList(
        @Param("nameAndLastNames") List<String> nameAndLastNames, Pageable pageable);
}
