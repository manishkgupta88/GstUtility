package org.example.service.processors;

import org.example.model.GstSheet;
import org.example.model.ItemSummarySheet;

/**
 * User : Manish K. Gupta
 */

public class ItemSummaryProcessor extends AbstractItemProcessor {
    @Override
    public GstSheet getSheetObj() {
        return new ItemSummarySheet();
    }
}
