package org.example.service;

import org.example.util.Constants;

/**
 * User : Manish K. Gupta
 */

public class ExcelProcessorFactory {

    public static IExcelProcessor getExcelProcessor(int index) {
        switch (index) {
            case Constants.GstR1Sheet:
                return new Gst1ReportProcessor();
        }

        return null;
    }
}
