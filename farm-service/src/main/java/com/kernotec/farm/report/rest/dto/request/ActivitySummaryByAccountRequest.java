package com.kernotec.farm.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySummaryByAccountRequest extends BaseRequest {

    private UUID socialNetworkId;
    private TemporyDateForRequestEnum filterDate;
    private ZonedDateTime monthDate;
    private UUID authUserId;
    private String zoneId;

    @JsonIgnore
    @Schema(hidden = true)
    private Pageable pageable;
}
