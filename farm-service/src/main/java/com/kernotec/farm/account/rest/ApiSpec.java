package com.kernotec.farm.account.rest;

import com.kernotec.farm.ApiSpecMain;

public class ApiSpec {

    public static final class AccountSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/accounts";
        public static final String TAG_NAME = "ACCOUNT";
        public static final String TAG_DESCRIPTION = "Account management";

        private AccountSpec() {
        }
    }

    public static final class PersonSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/persons";
        public static final String TAG_NAME = "PERSON";
        public static final String TAG_DESCRIPTION = "Person management";

        private PersonSpec() {
        }
    }
}
