package org.example.service.processors;

import org.example.model.B2ClSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2ClProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new B2ClSheet();
    }
}
