package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.GstSheet;
import org.example.util.Constants;
import org.example.util.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class ExcelConsolidator {

    public void consolidate(String path) {
        if (StringUtils.isEmpty(path)) {
            System.out.println("Folder path is empty or invalid");
            return;
        }
        File folder = new File(path);
        if (!folder.exists()) {
            System.out.println("Folder path does not exist");
            return;
        }
        System.out.println("Folder " + folder.getPath() + "accessed and read");
        processFiles(folder);
    }

    private void processFiles(File folder) {
        try {
            Workbook workbook = new XSSFWorkbook();
            for (int sheetCount = 0; sheetCount < Constants.ExcelFile.MaxSheets; sheetCount++) {
                IExcelProcessor processor = ExcelProcessorFactory.getExcelProcessor(sheetCount);
                List<GstSheet> objs = readSheetInAllFiles(folder, processor, sheetCount);
//                GstSheet gstSheet = processor.merge(objs);
//                Sheet wbSheet = workbook.createSheet(gstSheet.getName());
//                processor.write(wbSheet, gstSheet);
            }
            createOutputFile(workbook, folder.getPath());
        } catch (Exception e) {
            System.out.print("Error processing files");
            e.printStackTrace();
        }
    }

    private List<GstSheet> readSheetInAllFiles(File folder, IExcelProcessor processor, int sheetCount) throws Exception {
        List<GstSheet> objs = new ArrayList<>();
        int fileCount = 0;
        for (File file : folder.listFiles()) {
            if (!file.getName().endsWith("xls") && !file.getName().endsWith("xlsx")) {
                System.out.println("Skipping file: " + file.getName());
                continue;
            }
            ++fileCount;
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(sheetCount);
            System.out.println("Processing sheet: '" + sheet.getSheetName() + "' from file: '" + file.getName() + "'");
            if (processor == null) {
                System.out.println("Could not find processor for sheet: " + sheet.getSheetName());
                throw new Exception("Processor not found");
            }
            GstSheet gstSheet = processor.read(sheet);
            if (gstSheet != null) {
                gstSheet.setName(sheet.getSheetName());
                objs.add(gstSheet);
            }
        }
        return objs;
    }

    private void createOutputFile(Workbook workbook, String path) {
        String outputPath =
                Helper.getOutputPath(path).concat(File.separator).concat(Constants.ExcelFile.OutputFileName).concat(String.valueOf(System.currentTimeMillis())).concat(".xls");
        System.out.println("Output file " + outputPath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            System.out.print("Error writing the output file");
            e.printStackTrace();
        }
    }
}
