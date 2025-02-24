package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.GstSheet;
import org.example.util.Constants;
import org.example.util.Helper;

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
        String outputPath = Helper.getOutputPath(path);
        System.out.println("Output file " + outputPath);
        processFiles(folder);
    }

    private void processFiles(File folder) {
        try {
            for (int sheetCount = 0; sheetCount < Constants.ExcelFile.MaxSheets; sheetCount++) {
                IExcelProcessor processor = ExcelProcessorFactory.getExcelProcessor(sheetCount);
                List<GstSheet> objs = readSheet(folder, processor, sheetCount);
                if (!objs.isEmpty()) {
                    GstSheet finalSheet = processor.merge(objs);
                    processor.write(folder.getParent(), finalSheet);
                }
            }
        } catch (Exception e) {
            System.out.print("Error reading files");
            e.printStackTrace();
        }
    }

    private List<GstSheet> readSheet(File folder, IExcelProcessor processor, int sheetCount) throws Exception {
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
                objs.add(gstSheet);
            }
        }
        return objs;
    }

    /*
    public static <T> void writeDataToExcel(String filePath, MainDto mainDto) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNum = 0;

        // Creating header row
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"Field1", "Field2", "SubItem1", "SubItem2"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderStyle(workbook));
        }

        // Writing main object fields
        for (SubDto subDto : mainDto.getSubDtoList()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(mainDto.getField1());
            row.createCell(1).setCellValue(mainDto.getField2());
            row.createCell(2).setCellValue(subDto.getSubField1());
            row.createCell(3).setCellValue(subDto.getSubField2());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    * */
}
