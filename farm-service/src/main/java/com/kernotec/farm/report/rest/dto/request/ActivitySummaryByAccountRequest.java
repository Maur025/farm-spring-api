package com.kernotec.farm.report.rest.dto.request;

import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private UUID socialNetworkId;
    @NotNull
    private TemporyDateForRequestEnum filterDate;
    @NotNull
    private String zoneId;
    private Pageable pageable;
}
