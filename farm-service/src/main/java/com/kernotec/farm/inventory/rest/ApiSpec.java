package com.kernotec.farm.inventory.rest;

import com.kernotec.farm.ApiSpecMain;

public class ApiSpec {

    public static final class DeviceSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/devices";
        public static final String TAG_NAME = "DEVICE";
        public static final String TAG_DESCRIPTION = "Device management";

        private DeviceSpec() {
        }
    }

    public static final class FarmSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/farms";
        public static final String TAG_NAME = "FARM";
        public static final String TAG_DESCRIPTION = "Farm management";

        private FarmSpec() {
        }
    }
}
