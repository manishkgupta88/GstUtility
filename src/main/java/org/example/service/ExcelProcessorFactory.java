package org.example.service;

import org.example.service.processors.B2BProcessor;
import org.example.service.processors.B2ClProcessor;
import org.example.service.processors.Gst1ReportProcessor;
import org.example.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */

public class ExcelProcessorFactory {

    private static final Map<Integer, IExcelProcessor> processorMap = new HashMap<>();

    static {
        processorMap.put(Constants.GstR1Sheet, new Gst1ReportProcessor());
        processorMap.put(Constants.B2BSheet, new B2BProcessor());
        processorMap.put(Constants.B2ClSheet, new B2ClProcessor());
    }

    public static IExcelProcessor getExcelProcessor(int index) throws Exception {
        IExcelProcessor processor = processorMap.get(index);
        if (processor == null) {
            throw new Exception("Process not defined for this sheet");
        }
        return processor;
    }
}
