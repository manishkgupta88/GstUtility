package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstR1Report;
import org.example.model.GstSheet;
import org.example.model.InvoiceRecord;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor implements IExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        GstR1Report sheetObj = new GstR1Report();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 7) {
                break;
            }
        }
        List<InvoiceRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            InvoiceRecord record = parseInvoiceRow(row);
            if (StringUtils.isEmpty(record.getPartyName())) {
                break;
            }
            records.add(record);
        }
        sheetObj.setRecords(records);
        parseTotalRow(sheetObj, row);
        return sheetObj;
    }

    private void parseTotalRow(GstR1Report sheetObj, Row row) {
        sheetObj
                .setTotalInvoiceValue(Helper.getCellValueAsDouble(row.getCell(5)))
                .setTotalTaxableValue(Helper.getCellValueAsDouble(row.getCell(8)))
                .setTotalTaxAmount(Helper.getCellValueAsDouble(row.getCell(10)))
                .setTotalCentralTaxAmount(Helper.getCellValueAsDouble(row.getCell(11)))
                .setTotalStateTaxAmount(Helper.getCellValueAsDouble(row.getCell(12)))
                .setTotalCessAmount(Helper.getCellValueAsDouble(row.getCell(13)));
    }

    private InvoiceRecord parseInvoiceRow(Row row) {
        InvoiceRecord record = new InvoiceRecord();
        record.setGstin(Helper.getCellValueAsString(row.getCell(0)))
                .setPartyName(Helper.getCellValueAsString(row.getCell(1)))
                .setTransactionType(Helper.getCellValueAsString(row.getCell(2)))
                .setInvoiceNo(Helper.getCellValueAsString(row.getCell(3)))
                .setInvoiceDate(Helper.getCellValueAsString(row.getCell(4)))
                .setInvoiceValue(Helper.getCellValueAsString(row.getCell(5)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(6)))
                .setCessRate(Helper.getCellValueAsString(row.getCell(7)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(8)))
                .setReverseCharge(Helper.getCellValueAsString(row.getCell(9)))
                .setTaxAmount(Helper.getCellValueAsString(row.getCell(10)))
                .setCentralTaxAmount(Helper.getCellValueAsString(row.getCell(11)))
                .setStateTaxAmount(Helper.getCellValueAsString(row.getCell(12)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(13)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(14)));
        return record;
    }

    private void parseRow(int rc, Row row, GstR1Report sheetObj) {
        switch (rc) {
            case 1:
                DataPair period =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setPeriod(period);
                break;
            case 3:
                DataPair gstin =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setGstin(gstin);
                break;
            case 4:
                DataPair legalName =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setLegalName(legalName);
                break;
            case 5:
                DataPair traderName =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setTradeName(traderName);
                break;
            case 6:
                DataPair atpfy =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setAggregateTurnoverOfPrecedingFinancialYear(atpfy);
                break;
            case 7:
                DataPair at =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                sheetObj.setAggregateTurnover(at);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        GstR1Report finalSheet = (GstR1Report) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                GstR1Report sheet = (GstR1Report) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setTotalInvoiceValue(finalSheet.getTotalInvoiceValue() + sheet.getTotalInvoiceValue());
                finalSheet.setTotalTaxableValue(finalSheet.getTotalTaxableValue() + sheet.getTotalTaxableValue());
                finalSheet.setTotalTaxAmount(finalSheet.getTotalTaxAmount() + sheet.getTotalTaxAmount());
                finalSheet.setTotalCentralTaxAmount(finalSheet.getTotalCentralTaxAmount() + sheet.getTotalCentralTaxAmount());
                finalSheet.setTotalStateTaxAmount(finalSheet.getTotalStateTaxAmount() + sheet.getTotalStateTaxAmount());
                finalSheet.setTotalCessAmount(finalSheet.getTotalCessAmount() + sheet.getTotalCessAmount());
            }
        }
        return finalSheet;
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
