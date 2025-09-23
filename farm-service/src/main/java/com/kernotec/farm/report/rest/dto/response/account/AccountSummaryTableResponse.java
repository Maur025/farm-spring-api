package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountSummaryTableResponse extends EntityResponse {

    private UUID id;
    private String name;
    private String detail;
}
