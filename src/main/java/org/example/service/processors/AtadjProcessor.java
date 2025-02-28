package org.example.service.processors;

import org.example.model.AtadjSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class AtadjProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getSheetObj() {
        return new AtadjSheet();
    }

}
