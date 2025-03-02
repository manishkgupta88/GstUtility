package org.manitech.service.processors;

import org.manitech.model.AtSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class AtProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new AtSheet();
    }
}
