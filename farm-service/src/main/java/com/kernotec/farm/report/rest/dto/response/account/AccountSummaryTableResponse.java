package com.kernotec.farm.report.rest.dto.response.account;

import java.util.UUID;
import lombok.Builder;

@Builder
public record AccountSummaryTableResponse(UUID id, String name, String detail) {

}
