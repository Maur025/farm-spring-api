package com.kernotec.farm.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.report.jpa.enums.RatingTypeEnum;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class ReportRatingRequest extends BaseRequest {

    private ZonedDateTime simpleDate;
    private ZonedDateTime monthDate;
    private UUID socialNetworkId;
    private RatingTypeEnum ratingType;
    private Integer limit;
    private String zoneId;
}
