package org.example.service;

import org.example.service.processors.*;
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
        processorMap.put(Constants.B2csSheet, new B2csProcessor());
        processorMap.put(Constants.CdnrSheet, new CdnrProcessor());
        processorMap.put(Constants.CdnurSheet, new CdnurProcessor());
        processorMap.put(Constants.AtSheet, new AtProcessor());
        processorMap.put(Constants.AtadjSheet, new AtadjProcessor());
        processorMap.put(Constants.ExempSheet, new ExempProcessor());
    }

    public static IExcelProcessor getExcelProcessor(int index) throws Exception {
        IExcelProcessor processor = processorMap.get(index);
        if (processor == null) {
            throw new Exception("Process not defined for this sheet");
        }
        return processor;
    }
}
