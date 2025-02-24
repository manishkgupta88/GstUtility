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

public class B2BProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        B2BSheet sheetObj = new B2BSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<B2BInvoice> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            B2BInvoice record = parseInvoiceRow(row);
            records.add(record);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private B2BInvoice parseInvoiceRow(Row row) {
        B2BInvoice record = new B2BInvoice();
        record.setGstin(Helper.getCellValueAsString(row.getCell(0)))
                .setPartyName(Helper.getCellValueAsString(row.getCell(1)))
                .setInvoiceNo(Helper.getCellValueAsString(row.getCell(2)))
                .setInvoiceDate(Helper.getCellValueAsString(row.getCell(3)))
                .setInvoiceValue(Helper.getCellValueAsString(row.getCell(4)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(5)))
                .setReverseCharge(Helper.getCellValueAsString(row.getCell(6)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(7)))
                .setInvoiceType(Helper.getCellValueAsString(row.getCell(8)))
                .setEcommGstin(Helper.getCellValueAsString(row.getCell(9)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(10)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(11)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(12)));
        return record;
    }

    private void parseRow(int rc, Row row, B2BSheet b2bSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                b2bSheet.setTitle(title);
                break;
            case 2:
                DataPair recipients = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                DataPair invoices = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(2)));
                DataPair invoiceValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(4)));
                DataPair taxableValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(11)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(12)));
                b2bSheet.setRecipients(recipients);
                b2bSheet.setInvoices(invoices);
                b2bSheet.setInvoiceValue(invoiceValue);
                b2bSheet.setTaxableValue(taxableValue);
                b2bSheet.setTotalCess(totalCess);
                break;
            case 3:
                int recipientCount = Helper.getCellValueAsInt(row.getCell(0));
                int invoiceCount = Helper.getCellValueAsInt(row.getCell(2));
                Double invoiceValueDbl = Helper.getCellValueAsDouble(row.getCell(4));
                Double taxableValueDbl = Helper.getCellValueAsDouble(row.getCell(11));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(12));
                b2bSheet.getRecipients().setValue(String.valueOf(recipientCount));
                b2bSheet.getInvoices().setValue(String.valueOf(invoiceCount));
                b2bSheet.getInvoiceValue().setValue(String.valueOf(invoiceValueDbl));
                b2bSheet.getTaxableValue().setValue(String.valueOf(taxableValueDbl));
                b2bSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));
                b2bSheet.setNumOfRecipients(recipientCount);
                b2bSheet.setNumOfInvoices(invoiceCount);
                b2bSheet.setTotalInvoiceValue(invoiceValueDbl);
                b2bSheet.setTotalTaxableValue(taxableValueDbl);
                b2bSheet.setTotalCessAmount(totalCessDbl);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        B2BSheet finalSheet = (B2BSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                B2BSheet sheet = (B2BSheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setNumOfRecipients(finalSheet.getNumOfRecipients() + sheet.getNumOfRecipients());
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
