package org.manitech.service.processors;

import org.manitech.model.GstSheet;
import org.manitech.model.ItemSummarySheet;

/**
 * User : Manish K. Gupta
 */

public class ItemSummaryProcessor extends AbstractItemProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new ItemSummarySheet();
    }
}
