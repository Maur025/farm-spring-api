package com.kernotec.farm.account.jpa.enums;

import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;

public enum AccountTypeEnum {
    INTERNAL, EXTERNAL;

    public static AccountTypeEnum fromValue(ConnectionTypeEnum value) {
        if (value == null) {
            return null;
        }

        for (AccountTypeEnum entry : values()) {
            if (entry.toString()
                .equals(value.toString()))
            {
                return entry;
            }
        }

        return null;
    }
}
