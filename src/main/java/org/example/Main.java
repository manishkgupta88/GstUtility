package org.example;

import org.example.service.ExcelConsolidator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\guptamanis\\Desktop\\gst\\files";
        ExcelConsolidator reader = new ExcelConsolidator();
        reader.consolidate(folderPath);
    }
}