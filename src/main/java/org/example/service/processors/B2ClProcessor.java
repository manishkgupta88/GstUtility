package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.B2ClSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2ClProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        B2ClSheet sheetObj = new B2ClSheet();
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
