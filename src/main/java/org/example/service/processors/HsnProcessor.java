package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.GstSheet;
import org.example.model.HsnSheet;

/**
 * User : Manish K. Gupta
 */

public class HsnProcessor extends AbstractItemProcessor {
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
        return sheetObj;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
