package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.GstSheet;
import org.example.util.Constants;

import java.io.File;
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
        executeFiles(folder);
    }

    private void executeFiles(File folder) {
        try {
            int fileCount = 0;
            for (int i = 0; i < Constants.MaxSheets; i++) {
                IExcelProcessor processor = ExcelProcessorFactory.getExcelProcessor(i);
                fileCount = 0;
                List<GstSheet> objs = new ArrayList<>();
                for (File file : folder.listFiles()) {
                    if (!file.getName().endsWith("xls") && !file.getName().endsWith("xlsx")) {
                        System.out.println("Skipping file: " + file.getName());
                        continue;
                    }
                    ++fileCount;
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(i);
                    System.out.println("Processing sheet: '" + sheet.getSheetName() + "' from file: '" + file.getName() + "'");
                    if (processor == null) {
                        System.out.println("Could not find processor for sheet: " + sheet.getSheetName());
                        throw new Exception("Processor not found");
                    }
                    GstSheet gstSheet = processor.read(sheet);
                    if (gstSheet != null) {
                        objs.add(gstSheet);
                    }
                }
                processor.write(folder.getParent(), objs, fileCount == 1);
            }
        } catch (Exception e) {
            System.out.print("Error reading files");
            e.printStackTrace();
        }
    }
}
