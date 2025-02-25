package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.B2BSheet;
import org.example.model.GstSheet;

import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class B2BProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        B2BSheet sheetObj = new B2BSheet();
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
