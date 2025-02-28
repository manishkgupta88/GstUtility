package org.example.service.processors;

import org.example.model.B2BSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2BProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getSheetObj() {
        return new B2BSheet();
    }
}
