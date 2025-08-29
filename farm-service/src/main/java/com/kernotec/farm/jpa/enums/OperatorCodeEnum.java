package com.kernotec.farm.jpa.enums;

public enum OperatorCodeEnum {
    TIGO, VIVA, ENTEL;

    public static OperatorCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (OperatorCodeEnum entry : values()) {
            if (entry.toString()
                .equals(value.toUpperCase()))
            {
                return entry;
            }
        }

        return null;
    }
}
