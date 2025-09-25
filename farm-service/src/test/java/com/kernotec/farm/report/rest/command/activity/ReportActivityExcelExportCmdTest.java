package com.kernotec.farm.report.rest.command.activity;


import com.kernotec.core.test.UnitTest;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;

class ReportActivityExcelExportCmdTest extends UnitTest {

    @InjectMocks
    private ReportActivityExcelExportCmd reportActivityExcelExportCmd;

    @BeforeEach
    public void openMocks() {
        super.openMocks();
    }

    @DisplayName("Should export a demo report of activities to Excel")
    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void shouldExportDemoReportToExcel() {
        reportActivityExcelExportCmd.withRequest(ReportActivityExcelExportCmd.Request.builder()
                .build())
            .execute();
    }
}