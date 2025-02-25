package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.ExempSheet;
import org.example.model.GstSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class ExempProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        ExempSheet sheetObj = new ExempSheet();
        readRowPairs(sheet, sheetObj);
        readColumnPairs(sheet, sheetObj);
        readSummary(sheet, sheetObj);
        readTableHeaders(sheet, sheetObj);
        readRecords(sheet, sheetObj);
        return sheetObj;
    }

    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        ExempSheet finalSheet = (ExempSheet) gstSheets.get(0);
        if (gstSheets.size() > 1) {
            for (int i = 1; i < gstSheets.size(); i++) {
                ExempSheet sheet = (ExempSheet) gstSheets.get(i);
                if (finalSheet.getRecords() == null) {
                    finalSheet.setRecords(new ArrayList<>());
                }
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                finalSheet.setTotalNilRatedSupplies(finalSheet.getTotalNilRatedSupplies() + sheet.getTotalNilRatedSupplies());
                finalSheet.setTotalExemptedSupplies(finalSheet.getTotalExemptedSupplies() + sheet.getTotalExemptedSupplies());
                finalSheet.setTotalNonGstSupplies(finalSheet.getTotalNonGstSupplies() + sheet.getTotalNonGstSupplies());

            }
        }
        return finalSheet;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
