package com.kernotec.farm.activity.jpa.enums;

import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;

public enum ConnectionTypeEnum {
    INTERNAL, EXTERNAL;

    public static ConnectionTypeEnum fromValue(AccountTypeEnum value) {
        if (value == null) {
            return null;
        }

        for (ConnectionTypeEnum entry : values()) {
            if (entry.toString()
                .equals(value.toString()))
            {
                return entry;
            }
        }

        return null;
    }
}
