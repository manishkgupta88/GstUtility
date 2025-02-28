package org.example.service.processors;

import org.example.model.CdnurSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class CdnurProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new CdnurSheet();
    }
}
