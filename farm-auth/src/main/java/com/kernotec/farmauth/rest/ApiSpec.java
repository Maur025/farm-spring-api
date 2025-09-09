package com.kernotec.farmauth.rest;

public class ApiSpec {

    public static final String ROOT_PATH = "/realms/farm-auth";

    public static class OpenIdConfigurationSpec {

        public static final String BASE_PATH = ".well-known/openid-configuration";
        public static final String TAG_NAME = "OPENID CONFIGURATION";
        public static final String TAG_DESCRIPTION = "OpenID Connect discovery document";

        private OpenIdConfigurationSpec() {
        }
    }

    public static class UserSpec {

        public static final String BASE_PATH = ROOT_PATH + "/users";
        public static final String TAG_NAME = "USERS";
        public static final String TAG_DESCRIPTION = "User management";

        private UserSpec() {
        }
    }
}
