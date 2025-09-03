package com.kernotec.farm.account.rest.mapper.account;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kernotec.core.test.unit.test.util.AbstractMapperTest;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.account.rest.mapper.account.AccountResponseMapperImpl;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

class AccountResponseMapperTest extends AbstractMapperTest<Account, AccountResponse> {

    @InjectMocks
    private AccountResponseMapperImpl accountResponseMapper;

    private Account account;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        account = new Account();
        account.setId(UUID.randomUUID());

        account.setUsername("testuser");
        account.setPassword("password123");

        account.setPersonId(UUID.randomUUID());
        account.setPerson(new Person());

        account.setSocialNetworkId(UUID.randomUUID());
        account.setSocialNetwork(new SocialNetwork());

        account.setDevices(Set.of(new Device()));

        setInputObject(account);
    }

    @Override
    protected AccountResponse executeMapper(Account account) {
        return accountResponseMapper.toResponse(account);
    }

    @Override
    protected List<AccountResponse> executeMapper(List<Account> accountList) {
        return accountResponseMapper.toResponse(accountList);
    }

    @Override
    protected Set<AccountResponse> executeMapper(Set<Account> accountSet) {
        return accountResponseMapper.toResponse(accountSet);
    }

    @Override
    protected void assertsForResult(AccountResponse accountResponse) {
        assertAll(
            "AccountResponse asserts", () -> assertNotNull(accountResponse.getId()),
            () -> assertEquals(account.getId(), accountResponse.getId()),

            () -> assertNotNull(accountResponse.getUsername()),
            () -> assertEquals(account.getUsername(), accountResponse.getUsername()),

            () -> assertNotNull(accountResponse.getPassword()),
            () -> assertEquals(account.getPassword(), accountResponse.getPassword()),

            () -> assertNotNull(accountResponse.getPersonId()),
            () -> assertEquals(account.getPersonId(), accountResponse.getPersonId()),
            () -> assertNotNull(accountResponse.getPerson()),

            () -> assertNotNull(accountResponse.getSocialNetworkId()),
            () -> assertEquals(account.getSocialNetworkId(), accountResponse.getSocialNetworkId()),
            () -> assertNotNull(accountResponse.getSocialNetwork())
        );
    }
}