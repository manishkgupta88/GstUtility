package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.B2csSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2csProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        B2csSheet sheetObj = new B2csSheet();
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
