package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.GstR1Report;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        GstR1Report sheetObj = new GstR1Report();
        readRowPairs(sheet, sheetObj);
        readTableHeaders(sheet, sheetObj);
        readRecords(sheet, sheetObj);
        readSummary(sheet, sheetObj);
        return sheetObj;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {
        System.out.println("test");

        /*

        // Creating header row
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"Field1", "Field2", "SubItem1", "SubItem2"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderStyle(workbook));
        }

        // Writing main object fields
        for (SubDto subDto : mainDto.getSubDtoList()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(mainDto.getField1());
            row.createCell(1).setCellValue(mainDto.getField2());
            row.createCell(2).setCellValue(subDto.getSubField1());
            row.createCell(3).setCellValue(subDto.getSubField2());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
         */
    }
}
