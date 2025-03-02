package org.manitech.service.processors;

import org.manitech.model.CdnurSheet;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class CdnurProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new CdnurSheet();
    }
}
