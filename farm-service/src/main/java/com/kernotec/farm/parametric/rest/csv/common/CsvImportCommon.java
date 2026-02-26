package com.kernotec.farm.parametric.rest.csv.common;

import com.kernotec.farm.account.jpa.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CsvImportCommon {

    public String getChipUniqueCode(String phoneNumber, String index) {
        if (phoneNumber == null) {
            return index;
        }

        if (phoneNumber.equalsIgnoreCase("N/A")) {
            return String.format("%s|%s", phoneNumber, index);

        }

        return phoneNumber;
    }

    public String getAccountUniqueCode(String socialNetworkCode, String username) {
        return String.format("%s|%s", socialNetworkCode, username);
    }

    public String getFarmCode(String name) {
        return "FARM_" + name;
    }

    public String getPersonCode(String name, String lastName) {
        return String.format("%s|%s", name, lastName);
    }

    public Person getPersonEntity(String personName, String personLastName) {
        var person = new Person();

        person.setName(personName);
        person.setLastName(personLastName);

        return person;
    }
}
