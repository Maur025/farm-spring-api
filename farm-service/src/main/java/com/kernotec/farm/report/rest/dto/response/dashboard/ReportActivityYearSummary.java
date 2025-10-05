package com.kernotec.farm.report.rest.dto.response.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ReportActivityYearSummary extends EntityResponse {

    private Long totalActivities;
    private Long januaryActivities;
    private Long februaryActivities;
    private Long marchActivities;
    private Long aprilActivities;
    private Long mayActivities;
    private Long juneActivities;
    private Long julyActivities;
    private Long augustActivities;
    private Long septemberActivities;
    private Long octoberActivities;
    private Long novemberActivities;
    private Long decemberActivities;
}
