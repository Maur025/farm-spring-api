package com.kernotec.farm.parametric.jpa.enums;

public enum ActivityTypeCodeEnum {
    REACCION, COMENTARIO, PUBLICACION, GRUPO, AMISTAD, FOLLOW, FOLLOW_TIKTOK;

    public static ActivityTypeCodeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (ActivityTypeCodeEnum entry : values()) {
            if (value.equals(entry.toString())) {
                return entry;
            }
        }

        return null;
    }
}
