package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.GstSheet;
import org.example.model.HsnSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class HsnProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        HsnSheet sheetObj = new HsnSheet();
        readRowPairs(sheet, sheetObj);
        readColumnPairs(sheet, sheetObj);
        readSummary(sheet, sheetObj);
        readTableHeaders(sheet, sheetObj);
        readRecords(sheet, sheetObj);

        /*while (itr.hasNext()) {
            row = itr.next();
            HsnRecord record = parseInvoiceRow(row);
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
        sheetObj.setRecords(records);*/
        return sheetObj;
    }

    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        HsnSheet finalSheet = (HsnSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                HsnSheet sheet = (HsnSheet) gstSheets.get(i);
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
