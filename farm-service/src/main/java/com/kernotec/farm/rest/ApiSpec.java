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

    public static final class ActivitySpec {

        public static final String BASE_PATH = ROOT_PATH + "/activities";
        public static final String TAG_NAME = "ACTIVITY";
        public static final String TAG_DESCRIPTION = "Activity management";

        private ActivitySpec() {
        }
    }

    public static final class SocialNetworkSpec {

        public static final String BASE_PATH = ROOT_PATH + "/social-networks";
        public static final String TAG_NAME = "SOCIAL NETWORK";
        public static final String TAG_DESCRIPTION = "Social Network management";

        private SocialNetworkSpec() {
        }
    }

    public static final class PublishingContextSpec {

        public static final String BASE_PATH = ROOT_PATH + "/publishing-contexts";
        public static final String TAG_NAME = "PUBLISHING CONTEXT";
        public static final String TAG_DESCRIPTION = "Publishing Context management";

        private PublishingContextSpec() {
        }
    }

    public static final class PublishingTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/publishing-types";
        public static final String TAG_NAME = "PUBLISHING TYPE";
        public static final String TAG_DESCRIPTION = "Publishing Type management";

        private PublishingTypeSpec() {
        }
    }

    public static final class ActivityTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/activity-types";
        public static final String TAG_NAME = "ACTIVITY TYPE";
        public static final String TAG_DESCRIPTION = "Activity Type management";

        private ActivityTypeSpec() {
        }
    }

    public static final class DeviceSpec {

        public static final String BASE_PATH = ROOT_PATH + "/devices";
        public static final String TAG_NAME = "DEVICE";
        public static final String TAG_DESCRIPTION = "Device management";

        private DeviceSpec() {
        }
    }

    public static final class ReactionTypeSpec {

        public static final String BASE_PATH = ROOT_PATH + "/reaction-types";
        public static final String TAG_NAME = "REACTION TYPE";
        public static final String TAG_DESCRIPTION = "Reaction Type management";

        private ReactionTypeSpec() {
        }
    }

    public static final class DataImportSpec {

        public static final String BASE_PATH = ROOT_PATH + "/data-imports";
        public static final String TAG_NAME = "DATA IMPORT";
        public static final String TAG_DESCRIPTION = "Data Import management";

        private DataImportSpec() {
        }
    }

    public static final class RegionSpec {

        public static final String BASE_PATH = ROOT_PATH + "/regions";
        public static final String TAG_NAME = "REGION";
        public static final String TAG_DESCRIPTION = "Region management";

        private RegionSpec() {
        }
    }

    public static final class AccountSpec {

        public static final String BASE_PATH = ROOT_PATH + "/accounts";
        public static final String TAG_NAME = "ACCOUNT";
        public static final String TAG_DESCRIPTION = "Account management";

        private AccountSpec() {
        }
    }

    public static final class GroupSpec {

        public static final String BASE_PATH = ROOT_PATH + "/groups";
        public static final String TAG_NAME = "GROUP";
        public static final String TAG_DESCRIPTION = "Group management";

        private GroupSpec() {
        }
    }

    public static final class ConnectionSpec {

        public static final String BASE_PATH = ROOT_PATH + "/connections";
        public static final String TAG_NAME = "CONNECTION";
        public static final String TAG_DESCRIPTION = "Connection management";

        private ConnectionSpec() {
        }
    }

    public static final class FriendSpec {

        public static final String BASE_PATH = ROOT_PATH + "/friends";
        public static final String TAG_NAME = "FRIEND";
        public static final String TAG_DESCRIPTION = "Friend management";

        private FriendSpec() {
        }
    }
}
