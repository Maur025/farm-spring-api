package com.kernotec.farm.rest;

public class ApiSpec {

    protected static final String ROOT_PATH = "/api/farm";

    public static final class PersonSpec {

        public static final String BASE_PATH = ROOT_PATH + "/persons";
        public static final String TAG_NAME = "PERSON";
        public static final String TAG_DESCRIPTION = "Person management";

        private PersonSpec() {
        }
    }
}
