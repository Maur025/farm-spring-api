package com.kernotec.farm.parametric.rest.csv.dto.request;

import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AssignAccountRequest(@NotNull List<ImportExcelDataDto> importExcelDataDtoBatch,
                                   @NotNull Map<String, UUID> accountMap,
                                   @NotNull Map<String, UUID> deviceMap,
                                   @NotNull Map<String, UUID> chipMap)
{

}
