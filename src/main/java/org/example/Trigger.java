package org.example;

import org.example.service.ExcelConsolidator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Trigger {
    public static void main(String[] args) {
        String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files";
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);
    }
}