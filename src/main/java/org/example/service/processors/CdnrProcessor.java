package org.example.service.processors;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.CdnrSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class CdnrProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        CdnrSheet sheetObj = new CdnrSheet();
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
