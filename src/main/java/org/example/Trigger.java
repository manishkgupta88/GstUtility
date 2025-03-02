package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.ExcelConsolidator;
import org.example.util.Constants;
import org.example.util.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;


public class Trigger {
    private static final Logger logger = LogManager.getLogger(Trigger.class);

    public static void main(String[] args) throws URISyntaxException {
        logger.info("Starting trigger");
        String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files\\";
        logger.info("Using path: " + folderPath);
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);
        logger.info("Completed");
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