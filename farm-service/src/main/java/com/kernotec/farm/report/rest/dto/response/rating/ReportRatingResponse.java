package com.kernotec.farm.report.rest.dto.response.rating;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ReportRatingResponse extends EntityResponse {

    private UUID ratingById;
    private String ratingByName;
    private Long totalActivities;
}
