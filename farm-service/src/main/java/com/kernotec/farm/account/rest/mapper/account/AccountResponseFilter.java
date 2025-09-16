package com.kernotec.farm.account.rest.mapper.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Person;
import java.util.UUID;
import java.util.stream.Stream;

public class AccountResponseFilter {

    public static Stream<Account> getStreamWithFilters(Stream<Account> accountStream,
        UUID socialNetworkId, String keyword)
    {
        String pattern = keyword == null || keyword.isBlank() ? null : keyword.toLowerCase();

        if (socialNetworkId != null) {
            accountStream = accountStream.filter(account -> account.getSocialNetworkId()
                .equals(socialNetworkId));
        }

        if (pattern != null && !pattern.matches("-?\\d+")) {
            accountStream = accountStream.filter(
                account -> filterByUsername(account.getUsername(), pattern) || filterByPerson(
                    account.getPerson(), pattern));
        }

        return accountStream;
    }

    private static boolean filterByUsername(String username, String pattern) {
        return containsIgnoreCase(username, pattern);
    }

    private static boolean filterByPerson(Person person, String pattern) {
        if (person == null) {
            return false;
        }

        return containsIgnoreCase(person.getName(), pattern) || containsIgnoreCase(
            person.getLastName(), pattern);
    }

    private static boolean containsIgnoreCase(String value, String pattern) {
        return value != null && value.toLowerCase()
            .contains(pattern);
    }
}
