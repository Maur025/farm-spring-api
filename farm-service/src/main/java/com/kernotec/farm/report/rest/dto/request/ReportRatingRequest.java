package com.kernotec.farm.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farm.report.jpa.enums.RatingTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class ReportRatingRequest extends FilterDateRequest {

    private RatingTypeEnum ratingType;
    private UUID socialNetworkId;
    private Integer limit;
    private String searchCriteria;
}
