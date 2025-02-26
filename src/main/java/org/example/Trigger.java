package org.example;

import org.example.service.ExcelConsolidator;
import org.example.util.Constants;
import org.example.util.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;


public class Trigger {
    public static void main(String[] args) throws URISyntaxException {
        String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files\\";
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);
        //testOutPutFile(folderPath);
    }

    private static void testOutPutFile(String folderPath) {
        String outputPath =
                Helper.getOutputPath(folderPath).concat(File.separator).concat(Constants.ExcelFile.OutputFileName).concat(String.valueOf(System.currentTimeMillis())).concat(".xls");
        System.out.println("Output path: " + outputPath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
            outputStream.write("Test".getBytes());
            outputStream.close();
        } catch (Exception e) {
            System.out.print("Error writing the output file");
            e.printStackTrace();
        }
    }
}