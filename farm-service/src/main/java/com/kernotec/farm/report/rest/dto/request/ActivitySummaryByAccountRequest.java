package com.kernotec.farm.report.rest.dto.request;

import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySummaryByAccountRequest extends BaseRequest {

    private UUID socialNetworkId;
    private TemporyDateForRequestEnum filterDate;
}
