package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.B2ClInvoice;
import org.example.model.B2ClSheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class B2ClProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        B2ClSheet sheetObj = new B2ClSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<B2ClInvoice> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            B2ClInvoice record = parseInvoiceRow(row);
            records.add(record);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private B2ClInvoice parseInvoiceRow(Row row) {
        B2ClInvoice record = new B2ClInvoice();
        record.setInvoiceNo(Helper.getCellValueAsString(row.getCell(0)))
                .setInvoiceDate(Helper.getCellValueAsString(row.getCell(1)))
                .setInvoiceValue(Helper.getCellValueAsString(row.getCell(2)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(3)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(4)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(5)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(6)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(7)))
                .setEcommGstin(Helper.getCellValueAsString(row.getCell(8)));
        return record;
    }

    private void parseRow(int rc, Row row, B2ClSheet b2clSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                b2clSheet.setTitle(title);
                break;
            case 2:
                DataPair invoices = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                DataPair invoiceValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(2)));
                DataPair taxableValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(6)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(7)));
                b2clSheet.setInvoices(invoices);
                b2clSheet.setInvoiceValue(invoiceValue);
                b2clSheet.setTaxableValue(taxableValue);
                b2clSheet.setTotalCess(totalCess);
                break;
            case 3:
                int invoiceCount = Helper.getCellValueAsInt(row.getCell(0));
                Double invoiceValueDbl = Helper.getCellValueAsDouble(row.getCell(2));
                Double taxableValueDbl = Helper.getCellValueAsDouble(row.getCell(6));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(7));
                b2clSheet.getInvoices().setValue(String.valueOf(invoiceCount));
                b2clSheet.getInvoiceValue().setValue(String.valueOf(invoiceValueDbl));
                b2clSheet.getTaxableValue().setValue(String.valueOf(taxableValueDbl));
                b2clSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));
                b2clSheet.setNumOfInvoices(invoiceCount);
                b2clSheet.setTotalInvoiceValue(invoiceValueDbl);
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
        B2ClSheet finalSheet = (B2ClSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                B2ClSheet sheet = (B2ClSheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setNumOfInvoices(finalSheet.getNumOfInvoices() + sheet.getNumOfInvoices());
                finalSheet.setTotalInvoiceValue(finalSheet.getTotalInvoiceValue() + sheet.getTotalInvoiceValue());
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
