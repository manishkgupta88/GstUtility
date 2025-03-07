package org.manitech.service.processors;

import org.manitech.model.ExpSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class ExpProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new ExpSheet();
    }

}
