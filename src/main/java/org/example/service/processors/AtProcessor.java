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

public class AtProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        AtSheet sheetObj = new AtSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<AtRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            AtRecord record = parseInvoiceRow(row);
            records.add(record);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private AtRecord parseInvoiceRow(Row row) {
        AtRecord record = new AtRecord();
        record.setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(0)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(1)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(2)))
                .setGrossAdvanceTax(Helper.getCellValueAsString(row.getCell(3)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(4)));
        return record;
    }

    private void parseRow(int rc, Row row, AtSheet cdnrSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                cdnrSheet.setTitle(title);
                break;
            case 2:
                DataPair advanceTax = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(3)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(4)));
                cdnrSheet.setAdvanceTax(advanceTax);
                cdnrSheet.setTotalCess(totalCess);
                break;
            case 3:
                Double advanceTaxValue = Helper.getCellValueAsDouble(row.getCell(3));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(4));

                cdnrSheet.getAdvanceTax().setValue(String.valueOf(advanceTaxValue));
                cdnrSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));

                cdnrSheet.setTotalAdvanceValue(advanceTaxValue);
                cdnrSheet.setTotalCessAmount(totalCessDbl);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        AtSheet finalSheet = (AtSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                AtSheet sheet = (AtSheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setTotalAdvanceValue(finalSheet.getTotalAdvanceValue() + sheet.getTotalAdvanceValue());
                finalSheet.setTotalCessAmount(finalSheet.getTotalCessAmount() + sheet.getTotalCessAmount());
            }
        }
        return finalSheet;
    }

    @Override
    public void write(String path, GstSheet gstSheet) {

    }
}
