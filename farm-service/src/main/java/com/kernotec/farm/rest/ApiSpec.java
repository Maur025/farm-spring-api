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
}
