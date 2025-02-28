package org.example.service.processors;

import org.example.model.CdnrSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class CdnrProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new CdnrSheet();
    }
}
