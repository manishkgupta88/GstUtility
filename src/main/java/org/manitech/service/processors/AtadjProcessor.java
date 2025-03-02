package org.manitech.service.processors;

import org.manitech.model.AtadjSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class AtadjProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new AtadjSheet();
    }

}
