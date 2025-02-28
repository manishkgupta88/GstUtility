package org.example.service.processors;

import org.example.model.B2csSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2csProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getSheetObj() {
        return new B2csSheet();
    }
}
