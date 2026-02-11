package com.kernotec.farm.util;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelUtil {

    private final SwaggerConfigResource swaggerConfigResource;

    public void fillExcelRow(Row row, List<String> values, CellStyle style) {
        int columnCount = 0;

        for (String value : values) {
            Cell cell = row.createCell(columnCount);
            cell.setCellValue(value);

            cell.setCellStyle(style);

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

    public String getDateFormat(ZonedDateTime zonedDateTime, String zoneId, String pattern)
    {
        ZoneId userZone = getZoneId(zoneId);

        ZonedDateTime activityDateInUserZone = zonedDateTime.withZoneSameInstant(userZone);

        var formater = DateTimeFormatter.ofPattern(pattern);

        return activityDateInUserZone.format(formater);
    }

    public String getDateFormat(ZonedDateTime zonedDateTime, String pattern) {
        var formater = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formater);
    }

    private ZoneId getZoneId(String zoneId) {
        return zoneId != null ? ZoneId.of(zoneId) : ZoneId.systemDefault();
    }

    public void fillRowSingleColumn(Sheet sheet, String value, int indexRow, CellStyle style) {
        Row row = sheet.createRow(indexRow);
        fillExcelRow(row, List.of(value), style);
    }

    public File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
        } catch (IOException ex) {
            log.error(
                "Error creating temp file, with prefix:{}, with suffix:{}", prefix, suffix, ex);
            throw new RuntimeException(ex);
        }
    }
}
