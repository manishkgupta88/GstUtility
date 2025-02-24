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

public class ItemSummaryProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        ItemSummarySheet sheetObj = new ItemSummarySheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<ItemSummaryRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            ItemSummaryRecord record = parseInvoiceRow(row);
            records.add(record);
            ItemHsnKey itemHsnKey = sheetObj.getItemHsnKey(record.getHsn(), record.getRate());
            ItemHsn prevItemHsn = sheetObj.getHsnMap().get(itemHsnKey);
            if (prevItemHsn == null) {
                prevItemHsn = record.getItemHsn();
            } else {
                ItemHsn itemHsn = record.getItemHsn();
                prevItemHsn.merge(itemHsn);
            }
            sheetObj.getHsnMap().put(itemHsnKey, prevItemHsn);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private ItemSummaryRecord parseInvoiceRow(Row row) {
        ItemSummaryRecord record = new ItemSummaryRecord();
        record
                .setHsn(Helper.getCellValueAsString(row.getCell(0)))
                .setDescription(Helper.getCellValueAsString(row.getCell(1)))
                .setUnit(Helper.getCellValueAsString(row.getCell(2)))
                .setQuantity(Helper.getCellValueAsString(row.getCell(3)))
                .setValue(Helper.getCellValueAsString(row.getCell(4)))
                .setRate(Helper.getCellValueAsString(row.getCell(5)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(6)))
                .setIntegrateTax(Helper.getCellValueAsString(row.getCell(7)))
                .setCentralTax(Helper.getCellValueAsString(row.getCell(8)))
                .setStateTax(Helper.getCellValueAsString(row.getCell(9)))
                .setCess(Helper.getCellValueAsString(row.getCell(10)));

        return record;
    }

    private void parseRow(int rc, Row row, ItemSummarySheet sheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                sheet.setTitle(title);
                break;
            case 2:
                DataPair hsnPair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                DataPair totalValuePair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(4)));
                DataPair taxableValuePair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(6)));
                DataPair integratedTaxPair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(7)));
                DataPair centralTaxPair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(8)));
                DataPair stateTaxPair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(9)));
                DataPair cessPair = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(10)));
                sheet.setHsnCount(hsnPair);
                sheet.setTotalValue(totalValuePair);
                sheet.setTotalTaxableValue(taxableValuePair);
                sheet.setTotalIntegratedTax(integratedTaxPair);
                sheet.setTotalCentralTax(centralTaxPair);
                sheet.setTotalStateTax(stateTaxPair);
                sheet.setTotalCess(cessPair);
                break;
            case 3:
                int hsn = Helper.getCellValueAsInt(row.getCell(0));
                Double totalValue = Helper.getCellValueAsDouble(row.getCell(4));
                Double taxableValue = Helper.getCellValueAsDouble(row.getCell(6));
                Double integratedTax = Helper.getCellValueAsDouble(row.getCell(7));
                Double centralTax = Helper.getCellValueAsDouble(row.getCell(8));
                Double stateTax = Helper.getCellValueAsDouble(row.getCell(9));
                Double cess = Helper.getCellValueAsDouble(row.getCell(10));
                sheet.getHsnCount().setValue(String.valueOf(hsn));
                sheet.getTotalValue().setValue(String.valueOf(totalValue));
                sheet.getTotalTaxableValue().setValue(String.valueOf(taxableValue));
                sheet.getTotalIntegratedTax().setValue(String.valueOf(integratedTax));
                sheet.getTotalCentralTax().setValue(String.valueOf(centralTax));
                sheet.getTotalStateTax().setValue(String.valueOf(stateTax));
                sheet.getTotalCess().setValue(String.valueOf(cess));
                sheet.setNumOfHsn(hsn);
                sheet.setTotal(totalValue);
                sheet.setTaxableValue(taxableValue);
                sheet.setIntegrateTax(integratedTax);
                sheet.setCentralTax(centralTax);
                sheet.setStateTax(stateTax);
                sheet.setCess(cess);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        ItemSummarySheet finalSheet = (ItemSummarySheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                ItemSummarySheet sheet = (ItemSummarySheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setNumOfHsn(finalSheet.getNumOfHsn() + sheet.getNumOfHsn());
                finalSheet.setTotal(finalSheet.getTotal() + sheet.getTotal());
                finalSheet.setTaxableValue(finalSheet.getTaxableValue() + sheet.getTaxableValue());
                finalSheet.setIntegrateTax(finalSheet.getIntegrateTax() + sheet.getIntegrateTax());
                finalSheet.setCentralTax(finalSheet.getCentralTax() + sheet.getCentralTax());
                finalSheet.setStateTax(finalSheet.getStateTax() + sheet.getStateTax());
                finalSheet.setCess(finalSheet.getCess() + sheet.getCess());
            }
        }
        return finalSheet;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
