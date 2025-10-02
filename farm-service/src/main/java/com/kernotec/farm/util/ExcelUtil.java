package com.kernotec.farm.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springdoc.webmvc.ui.SwaggerConfigResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExcelUtil {

    private final SwaggerConfigResource swaggerConfigResource;

    public void fillExcelRow(Row row, boolean isBold, List<String> values, boolean withBackground) {
        int columnCount = 0;

        for (String value : values) {
            Cell cell = row.createCell(columnCount);
            cell.setCellValue(value);

            cell.setCellStyle(getCellStyle(
                row.getSheet()
                    .getWorkbook(), isBold, withBackground
            ));

            columnCount++;
        }
    }

    public CellStyle getCellStyle(Workbook workbook, boolean isBold, boolean withBackground) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(isBold);

        cellStyle.setFont(font);

        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        if (withBackground) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        return cellStyle;
    }

    public void setColumnsSize(Sheet sheet, List<Integer> columnsSize) {
        for (int index = 0; index < columnsSize.size(); index++) {
            sheet.setColumnWidth(index, columnsSize.get(index) * 256);
        }
    }

    public String getDateFormat(ZonedDateTime zonedDateTime, String zoneId)
    {
        ZoneId userZone = zoneId != null ? ZoneId.of(zoneId) : ZoneId.systemDefault();

        ZonedDateTime activityDateInUserZone = zonedDateTime.withZoneSameInstant(userZone);

        var formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return activityDateInUserZone.format(formater);
    }
}
