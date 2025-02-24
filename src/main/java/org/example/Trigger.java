package org.example;

import org.example.service.ExcelConsolidator;

import java.net.URISyntaxException;


public class Trigger {
    public static void main(String[] args) throws URISyntaxException {
        String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files\\";
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);

    }
}