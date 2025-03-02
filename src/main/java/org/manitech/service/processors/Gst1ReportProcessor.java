package org.manitech.service.processors;

import org.manitech.model.GstR1Report;
import org.manitech.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new GstR1Report();
    }
}
