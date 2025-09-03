package com.kernotec.farm.activity.rest;

import com.kernotec.farm.ApiSpecMain;

public class ApiSpec {

    public static final class ActivitySpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/activities";
        public static final String TAG_NAME = "ACTIVITY";
        public static final String TAG_DESCRIPTION = "Activity management";

        private ActivitySpec() {
        }
    }

    public static final class GroupSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/groups";
        public static final String TAG_NAME = "GROUP";
        public static final String TAG_DESCRIPTION = "Group management";

        private GroupSpec() {
        }
    }

    public static final class ConnectionSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/connections";
        public static final String TAG_NAME = "CONNECTION";
        public static final String TAG_DESCRIPTION = "Connection management";

        private ConnectionSpec() {
        }
    }

    public static final class FriendSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/friends";
        public static final String TAG_NAME = "FRIEND";
        public static final String TAG_DESCRIPTION = "Friend management";

        private FriendSpec() {
        }
    }
}
