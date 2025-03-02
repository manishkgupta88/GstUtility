package org.manitech.service.processors;

import org.manitech.model.GstSheet;
import org.manitech.model.HsnSheet;

/**
 * User : Manish K. Gupta
 */

public class HsnProcessor extends AbstractItemProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new HsnSheet();
    }
}
