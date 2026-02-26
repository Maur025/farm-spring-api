package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class HandleImportFactory {

    private final Map<HandleTypeEnum, HandleImport<?>> handlers;

    public HandleImportFactory(List<HandleImport<?>> handleImportList) {
        this.handlers = handleImportList.stream()
            .collect(Collectors.toMap(HandleImport::handleType, handler -> handler));
    }

    @SuppressWarnings("unchecked")
    public <R> HandleImport<R> getHandler(HandleTypeEnum handleType) {
        return Optional.ofNullable((HandleImport<R>) handlers.get(handleType))
            .orElseThrow(() -> new IllegalArgumentException(
                "No handler found for handle type: " + handleType));
    }
}
