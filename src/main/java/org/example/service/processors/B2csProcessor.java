package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.*;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class B2csProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        B2csSheet sheetObj = new B2csSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<B2csRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            B2csRecord record = parseInvoiceRow(row);
            records.add(record);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private B2csRecord parseInvoiceRow(Row row) {
        B2csRecord record = new B2csRecord();
        record.setType(Helper.getCellValueAsString(row.getCell(0)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(1)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(2)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(3)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(4)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(5)))
                .setEcommGstin(Helper.getCellValueAsString(row.getCell(6)));
        return record;
    }

    private void parseRow(int rc, Row row, B2csSheet b2clSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                b2clSheet.setTitle(title);
                break;
            case 2:
                DataPair taxableValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(4)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(5)));
                b2clSheet.setTaxableValue(taxableValue);
                b2clSheet.setTotalCess(totalCess);
                break;
            case 3:
                Double taxableValueDbl = Helper.getCellValueAsDouble(row.getCell(4));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(5));
                b2clSheet.getTaxableValue().setValue(String.valueOf(taxableValueDbl));
                b2clSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));
                b2clSheet.setTotalTaxableValue(taxableValueDbl);
                b2clSheet.setTotalCessAmount(totalCessDbl);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        B2csSheet finalSheet = (B2csSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                B2csSheet sheet = (B2csSheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setTotalTaxableValue(finalSheet.getTotalTaxableValue() + sheet.getTotalTaxableValue());
                finalSheet.setTotalCessAmount(finalSheet.getTotalCessAmount() + sheet.getTotalCessAmount());
            }
        }
        return finalSheet;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
