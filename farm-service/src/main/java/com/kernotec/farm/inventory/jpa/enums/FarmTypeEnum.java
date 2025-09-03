package com.kernotec.farm.inventory.jpa.enums;

public enum FarmTypeEnum {
    CELULAR, SERVIDOR;

    public static FarmTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (FarmTypeEnum entry : values()) {
            if (entry.toString()
                .equals(value.toUpperCase()))
            {
                return entry;
            }
        }

        return null;
    }
}
