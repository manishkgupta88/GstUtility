package org.manitech.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.manitech.model.DataPair;
import org.manitech.model.GstSheet;
import org.manitech.service.IExcelProcessor;
import org.manitech.util.Helper;

import java.util.LinkedList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public abstract class AbstractExcelProcessor implements IExcelProcessor {
    private static final Logger logger = LogManager.getLogger(AbstractExcelProcessor.class);

    public abstract GstSheet getGstSheetObj();

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            logger.error("Sheet found null while reading. Returning!");
            return null;
        }
        GstSheet gstSheet = getGstSheetObj();
        readRowPairs(sheet, gstSheet);
        readColumnPairs(sheet, gstSheet);
        if (!gstSheet.isSummaryInLastRow()) {
            readSummary(sheet, gstSheet);
        }
        readTableHeaders(sheet, gstSheet);
        readRecords(sheet, gstSheet);
        if (gstSheet.isSummaryInLastRow()) {
            readSummary(sheet, gstSheet);
        }
        return gstSheet;
    }

    private void readRowPairs(Sheet sheet, GstSheet gstSheet) {
        if (gstSheet.getRowPairCount() == -1) {
            logger.info("Row pair count not defined. Skipping for sheet:" + sheet.getSheetName());
            return;
        }
        logger.info("Reading " + gstSheet.getRowPairCount() + " row pair count");
        for (int i = 0; i < gstSheet.getRowPairCount(); i++) {
            Row row = sheet.getRow(i);
            LinkedList<DataPair> dataList = new LinkedList<>();
            if (row != null) {
                dataList.add(Helper.getCellData(row.getCell(0)));
                dataList.add(Helper.getCellData(row.getCell(1)));
            }
            gstSheet.getRowPairs().add(dataList);
        }
    }

    private void readColumnPairs(Sheet sheet, GstSheet gstSheet) {
        if (gstSheet.getCpRow() == -1) {
            logger.info("Skipping the column pairs as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = sheet.getRow(gstSheet.getCpRow());
        if (row != null) {
            for (int i = 0; i < gstSheet.getColumnPairCount(); i++) {
                Cell cell = row.getCell(i);
                gstSheet.getColumnPairs().add(Helper.getCellData(cell));
            }
        }
    }

    private void readSummary(Sheet sheet, GstSheet gstSheet) {
        int rc = gstSheet.getSummaryRow();
        if (gstSheet.isSummaryInLastRow()) {
            rc = gstSheet.getDataStartRow() + gstSheet.getRecords().size();
        }
        Row summrayRow = sheet.getRow(rc);
        if (summrayRow != null) {
            for (int i = 0; i < gstSheet.getColumnPairCount(); i++) {
                gstSheet.getSummaryList().add(Helper.getCellData(summrayRow.getCell(i)));
            }
        }
    }

    private void readTableHeaders(Sheet sheet, GstSheet gstSheet) {
        Row row = sheet.getRow(gstSheet.getDataStartRow() - 1);
        for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
            gstSheet.getTableHeaders().add(Helper.getCellData(row.getCell(i)));
        }
    }

    private void readRecords(Sheet sheet, GstSheet gstSheet) {
        for (int rc = gstSheet.getDataStartRow(); ; rc++) {
            Row row = sheet.getRow(rc);
            if (rc > sheet.getLastRowNum()) {
                return;
            }
            LinkedList<DataPair> cellDataList = new LinkedList<>();
            if (row != null) {
                for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
                    DataPair dataPair = Helper.getCellData(row.getCell(i));
                    if (gstSheet.isSummaryInLastRow() && i == 1 && StringUtils.isEmpty(dataPair.getValue())) {
                        return;
                    } else {
                        cellDataList.add(dataPair);
                    }
                }
            }
            gstSheet.getRecords().add(cellDataList);
        }
    }

    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        GstSheet finalSheet = gstSheets.get(0);
        mergeRecords(gstSheets, finalSheet);
        mergeSummary(gstSheets, finalSheet);
        computeUniqueCounts(finalSheet);
        return finalSheet;
    }

    private void mergeRecords(List<GstSheet> gstSheets, GstSheet finalSheet) {
        if (gstSheets.size() <= 1) {
            return;
        }
        if (finalSheet.getRecords() == null) {
            finalSheet.setRecords(new LinkedList<>());
        }
        for (int i = 1; i < gstSheets.size(); i++) {
            GstSheet sheet = gstSheets.get(i);
            if (sheet.getRecords() == null) {
                continue;
            }
            if (finalSheet.getRecords().size() >= 2) {
                LinkedList<DataPair> data = finalSheet.getRecords().get(finalSheet.getRecords().size() - 1);
                if (CollectionUtils.isEmpty(data)) {
                    finalSheet.getRecords().remove(finalSheet.getRecords().size() - 1);
                }
            }
            if (finalSheet.getRecords().size() > 1) {
                for (int j = 0; j < sheet.getRecords().size(); j++) {
                    LinkedList<DataPair> record = sheet.getRecords().get(j);
                    if (j == 0 && CollectionUtils.isEmpty(record)) {
                        continue;
                    }
                    finalSheet.getRecords().add(record);
                }
            } else {
                finalSheet.getRecords().addAll(sheet.getRecords());
            }
        }
    }

    protected void mergeSummary(List<GstSheet> gstSheets, GstSheet finalSheet) {
        if (gstSheets.size() <= 1) {
            return;
        }
        LinkedList<DataPair> summaryList = finalSheet.getSummaryList();
        for (int i = 1; i < gstSheets.size(); i++) {
            GstSheet sheet = gstSheets.get(i);
            for (int j = 0; j < sheet.getSummaryList().size(); j++) {
                DataPair dataPair = sheet.getSummaryList().get(j);
                if (StringUtils.isNotEmpty(dataPair.getValue())) {
                    DataPair finalPair = summaryList.get(j);
                    if (finalPair.getType() == CellType.NUMERIC) {
                        finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue())));
                    }
                }
            }
        }
    }

    protected void computeUniqueCounts(GstSheet finalSheet) {
        if (finalSheet.getUniqueCountIndexes() == null) {
            return;
        }
        for (int index : finalSheet.getUniqueCountIndexes()) {
            int uniqueCount = Helper.getUniqueCountFromList(finalSheet.getRecords(), index);
            finalSheet.getSummaryList().get(0).setValue(String.valueOf(uniqueCount));
        }
    }

    @Override
    public void write(Sheet sheet, GstSheet gstSheet) {
        if (sheet == null || gstSheet == null) {
            return;
        }
        System.out.println("Writing sheet: " + sheet.getSheetName());
        writeRowPairs(sheet, gstSheet);
        writeColumnPairs(sheet, gstSheet);
        if (!gstSheet.isSummaryInLastRow()) {
            writeSummary(sheet, gstSheet);
        }
        writeTableHeaders(sheet, gstSheet);
        writeRecords(sheet, gstSheet);
        if (gstSheet.isSummaryInLastRow()) {
            writeSummary(sheet, gstSheet);
        }
    }

    private void writeRowPairs(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getRowPairs())) {
            System.out.println("Skipping to write Sheet:" + sheet.getSheetName());
            return;
        }
        int rc = 0, cc;
        for (LinkedList<DataPair> dataPairList : gstSheet.getRowPairs()) {
            cc = 0;
            Row row = sheet.createRow(rc++);
            if (CollectionUtils.isEmpty(dataPairList)) {
                continue;
            }
            for (DataPair dataPair : dataPairList) {
                Cell cell = row.createCell(cc++);
                populateCellValue(dataPair, cell);
            }
        }
    }

    private void populateCellValue(DataPair dataPair, Cell cell) {
        if (dataPair == null || StringUtils.isEmpty(dataPair.getValue())) {
            cell.setBlank();
        } else {
            if (dataPair.getType() == CellType.NUMERIC) {
                Double valDbl = NumberUtils.toDouble(dataPair.getValue(), 0.0);
                if (valDbl == valDbl.intValue()) {
                    cell.setCellValue(valDbl.intValue());
                } else {
                    cell.setCellValue(valDbl);
                }
            } else {
                cell.setCellValue(dataPair.getValue());
            }
        }
    }

    private void writeColumnPairs(Sheet sheet, GstSheet gstSheet) {
        if (gstSheet.getCpRow() == -1 || CollectionUtils.isEmpty(gstSheet.getColumnPairs())) {
            System.out.println("Skipping the column pairs as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = sheet.createRow(gstSheet.getCpRow());
        int cc = 0;
        for (DataPair dataPair : gstSheet.getColumnPairs()) {
            Cell cell = row.createCell(cc++);
            populateCellValue(dataPair, cell);
        }
    }

    private void writeSummary(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getSummaryList())) {
            System.out.println("Skipping the summary row as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        int rc = gstSheet.isSummaryInLastRow() ? (gstSheet.getDataStartRow() + gstSheet.getRecords().size()) :
                gstSheet.getSummaryRow();
        Row row = sheet.createRow(rc);
        for (int i = 0; i < gstSheet.getSummaryList().size(); i++) {
            DataPair dataPair = gstSheet.getSummaryList().get(i);
            Cell cell = row.createCell(i);
            populateCellValue(dataPair, cell);
        }
    }

    private void writeTableHeaders(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getTableHeaders())) {
            System.out.println("Skipping the header row as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = sheet.createRow(gstSheet.getDataStartRow() - 1);
        for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
            sheet.autoSizeColumn(i);
            Cell cell = row.createCell(i);
            populateCellValue(gstSheet.getTableHeaders().get(i), cell);
        }
    }

    private void writeRecords(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getRecords())) {
            System.out.println("Skipping the table data as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = null;
        CellStyle textStyle = getTextCellStyle(sheet, gstSheet);
        for (int i = 0; i < gstSheet.getRecords().size(); i++) {
            LinkedList<DataPair> cellDataList = gstSheet.getRecords().get(i);
            row = sheet.createRow(i + gstSheet.getDataStartRow());
            if (CollectionUtils.isEmpty(cellDataList)) {
                continue;
            }
            for (int j = 0; j < gstSheet.getHeaderCount() && j < cellDataList.size(); j++) {
                Cell cell = row.createCell(j);
                populateCellValue(cellDataList.get(j), cell);
                if (gstSheet.getTextTypeCells() != null && textStyle != null) {
                    for (Integer index : gstSheet.getTextTypeCells()) {
                        if (index == j) {
                            cell.setCellStyle(textStyle);
                        }
                    }
                }
            }
        }
    }

    private CellStyle getTextCellStyle(Sheet sheet, GstSheet gstSheet) {
        CellStyle textStyle = null;
        if (gstSheet.getTextTypeCells() != null) {
            Workbook workbook = sheet.getWorkbook();
            textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
        }
        return textStyle;
    }
}
