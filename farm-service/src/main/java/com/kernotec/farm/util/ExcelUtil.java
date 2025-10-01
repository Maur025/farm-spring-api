package com.kernotec.farm.util;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExcelUtil {

    public void fillExcelRow(Row row, boolean isBold, List<String> values) {
        int columnCount = 0;

        for (String value : values) {
            Cell cell = row.createCell(columnCount);
            cell.setCellValue(value);

            cell.setCellStyle(getBoldStyle(
                row.getSheet()
                    .getWorkbook(), isBold
            ));

            columnCount++;
        }
    }

    public CellStyle getBoldStyle(Workbook workbook, boolean isBold) {
        CellStyle boldStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(isBold);

        boldStyle.setFont(font);

        return boldStyle;
    }
}
