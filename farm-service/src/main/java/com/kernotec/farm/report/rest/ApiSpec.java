package com.kernotec.farm.report.rest;

import com.kernotec.farm.ApiSpecMain;

public class ApiSpec {

    public static final class ReportSpec {

        public static final String BASE_PATH = ApiSpecMain.ROOT_PATH + "/reports";
        public static final String TAG_NAME = "REPORT";
        public static final String TAG_DESCRIPTION = "Report management";

        private ReportSpec() {
        }
    }
}
