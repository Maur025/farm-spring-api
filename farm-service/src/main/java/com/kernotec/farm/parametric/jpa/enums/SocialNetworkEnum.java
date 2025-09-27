package com.kernotec.farm.parametric.jpa.enums;

public enum SocialNetworkEnum {
    FACEBOOK, X, INSTAGRAM, YOUTUBE, TIKTOK, WHATSAPP, CORREO;

    public static SocialNetworkEnum fromValue(String value) {
        if (value != null && !value.isEmpty()) {
            for (SocialNetworkEnum entry : values()) {
                if (value.equals(entry.toString())) {
                    return entry;
                }
            }
        }

        return null;
    }
}
