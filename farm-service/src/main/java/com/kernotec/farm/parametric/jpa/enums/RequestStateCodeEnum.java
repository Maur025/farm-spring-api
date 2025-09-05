package com.kernotec.farm.parametric.jpa.enums;

import com.kernotec.farm.activity.jpa.enums.ConnectionResponseRequestStateEnum;

public enum RequestStateCodeEnum {
    PENDING, APPROVED, REJECTED, NOTHING_WAS_REQUESTED;

    public static RequestStateCodeEnum fromValue(ConnectionResponseRequestStateEnum value) {
        if (value == null) {
            return null;
        }

        for (RequestStateCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value.toString()))
            {
                return entry;
            }
        }

        return null;
    }
}
