package org.manitech.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.manitech.model.GstSheet;
import org.manitech.util.Constants;
import org.manitech.util.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class ExcelConsolidator {
    private static final Logger logger = LogManager.getLogger(ExcelConsolidator.class);

    public void consolidate(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            logger.error("Folder path is empty or invalid");
            return;
        }
        File folder = new File(path);
        if (!folder.exists()) {
            logger.error("Folder path does not exist");
            return;
        }
        logger.info("Folder " + folder.getPath() + "accessed and read");
        processFiles(folder);
    }

    private void processFiles(File folder) throws Exception {
        try {
            Workbook workbook = new XSSFWorkbook();
            for (int sheetCount = 0; sheetCount < Constants.ExcelFile.MaxSheets; sheetCount++) {
                IExcelProcessor processor = ExcelProcessorFactory.getExcelProcessor(sheetCount);
                logger.debug("Found processor:" + processor.getClass().getName());
                List<GstSheet> objs = readSheetInAllFiles(folder, processor, sheetCount);
                logger.debug("Sheets read and created list of size:" + objs.size());
                logger.info("Starting merge for sheet:" + sheetCount);
                GstSheet gstSheet = processor.merge(objs);
                logger.info("Merge completed for sheet:" + sheetCount);
                Sheet wbSheet = workbook.createSheet(gstSheet.getName());
                logger.info("Writing sheet for sheet:" + sheetCount);
                processor.write(wbSheet, gstSheet);
            }
            createOutputFile(workbook, folder.getPath());
        } catch (Exception e) {
            logger.error("Error processing files", e);
            throw e;
        }
    }

    private List<GstSheet> readSheetInAllFiles(File folder, IExcelProcessor processor, int sheetCount) throws Exception {
        logger.info("Reading from all files sheet:" + sheetCount);
        List<GstSheet> objs = new LinkedList<>();
        int fileCount = 0;
        for (File file : folder.listFiles()) {
            if (!file.getName().endsWith("xls") && !file.getName().endsWith("xlsx")) {
                logger.info("Skipping non xls/xlsx file: " + file.getName());
                continue;
            }
            ++fileCount;
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(sheetCount);
            logger.info("Processing sheet " + sheetCount + " with name '" + sheet.getSheetName() + "' from " +
                    "file: " + fileCount + " with file name: '" + file.getName() + "'");
            if (processor == null) {
                logger.error("Could not find processor for sheet: " + sheet.getSheetName());
                throw new Exception("Processor not found");
            }
            GstSheet gstSheet = processor.read(sheet);
            if (gstSheet != null) {
                gstSheet.setName(sheet.getSheetName());
                objs.add(gstSheet);
            }
            logger.info("Read sheet: " + sheetCount + " with name:" + gstSheet.getName());
            workbook.close();
        }
        return objs;
    }

    private void createOutputFile(Workbook workbook, String path) throws Exception {
        String outputPath =
                Helper.getOutputPath(path).concat(File.separator).concat(Constants.ExcelFile.OutputFileName).concat(String.valueOf(System.currentTimeMillis())).concat(".xlsx");
        logger.info("Output file " + outputPath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            logger.error("Error writing the output file", e);
            throw e;
        }
    }
}
