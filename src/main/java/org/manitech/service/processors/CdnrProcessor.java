package org.manitech.service.processors;

import org.manitech.model.CdnrSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class CdnrProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new CdnrSheet();
    }
}
