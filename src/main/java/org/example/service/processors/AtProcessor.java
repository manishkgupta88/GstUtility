package org.example.service.processors;

import org.example.model.AtSheet;
import org.example.model.GstSheet;

/**
 * User : Manish K. Gupta
 */

public class AtProcessor extends AbstractExcelProcessor {

    @Override
    public GstSheet getSheetObj() {
        return new AtSheet();
    }
}
