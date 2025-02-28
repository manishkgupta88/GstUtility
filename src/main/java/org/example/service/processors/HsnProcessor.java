package org.example.service.processors;

import org.example.model.GstSheet;
import org.example.model.HsnSheet;

/**
 * User : Manish K. Gupta
 */

public class HsnProcessor extends AbstractItemProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new HsnSheet();
    }
}
