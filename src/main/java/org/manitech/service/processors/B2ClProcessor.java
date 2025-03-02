package org.manitech.service.processors;

import org.manitech.model.B2ClSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2ClProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new B2ClSheet();
    }
}
