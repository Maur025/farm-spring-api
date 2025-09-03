package com.kernotec.farm.parametric.rest.controller;

import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.farm.parametric.rest.ApiSpec.DataImportSpec;
import com.kernotec.farm.parametric.rest.command.excel.ExcelImportCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = DataImportSpec.TAG_NAME, description = DataImportSpec.TAG_DESCRIPTION)
@RequestMapping(path = DataImportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class DataImportController {

    private final ExcelImportCmd excelImportCmd;

    @Operation(summary = "Import data from external sources")
    @PostMapping(value = "excel", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse importFromExcel(@RequestPart(value = "file") MultipartFile multipartFile)
    {

        excelImportCmd.withRequest(ExcelImportCmd.Request.builder()
                .excelFile(multipartFile)
                .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Data imported successfully")
            .build();
    }
}
