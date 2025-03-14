package org.manitech.service;

import org.manitech.service.processors.*;
import org.manitech.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */

public class ExcelProcessorFactory {

    private static final Map<Integer, IExcelProcessor> processorMap = new HashMap<>();

    static {
        processorMap.put(Constants.ExcelFile.GstR1Sheet, new Gst1ReportProcessor());
        processorMap.put(Constants.ExcelFile.B2BSheet, new B2BProcessor());
        processorMap.put(Constants.ExcelFile.B2ClSheet, new B2ClProcessor());
        processorMap.put(Constants.ExcelFile.B2csSheet, new B2csProcessor());
        processorMap.put(Constants.ExcelFile.CdnrSheet, new CdnrProcessor());
        processorMap.put(Constants.ExcelFile.CdnurSheet, new CdnurProcessor());
        processorMap.put(Constants.ExcelFile.ExpSheet, new ExpProcessor());
        processorMap.put(Constants.ExcelFile.AtSheet, new AtProcessor());
        processorMap.put(Constants.ExcelFile.AtadjSheet, new AtadjProcessor());
        processorMap.put(Constants.ExcelFile.ExempSheet, new ExempProcessor());
        processorMap.put(Constants.ExcelFile.HsnSheet, new HsnProcessor());
        processorMap.put(Constants.ExcelFile.ItemSummarySheet, new ItemSummaryProcessor());
    }

    public static IExcelProcessor getExcelProcessor(int index) throws Exception {
        IExcelProcessor processor = processorMap.get(index);
        if (processor == null) {
            throw new Exception("Process not defined for this sheet");
        }
        return processor;
    }
}
