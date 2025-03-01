package org.example.service.processors;

import org.example.model.GstR1Report;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getGstSheetObj() {
        return new GstR1Report();
    }
}
