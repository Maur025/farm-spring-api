package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

@Validated
public interface HandleImport<R> {

    HandleTypeEnum handleType();

    Map<String, UUID> handle(@Valid R r);
}
