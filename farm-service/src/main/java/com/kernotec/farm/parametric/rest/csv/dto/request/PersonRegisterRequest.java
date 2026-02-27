package com.kernotec.farm.parametric.rest.csv.dto.request;

import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record PersonRegisterRequest(@NotNull List<ImportExcelDataDto> importExcelDataDtoBatch) {

}
