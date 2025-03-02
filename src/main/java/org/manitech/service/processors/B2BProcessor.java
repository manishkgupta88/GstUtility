package org.manitech.service.processors;

import org.manitech.model.B2BSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class B2BProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new B2BSheet();
    }
}
