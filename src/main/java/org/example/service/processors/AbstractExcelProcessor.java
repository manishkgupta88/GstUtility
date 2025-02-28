package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.LinkedList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public abstract class AbstractExcelProcessor implements IExcelProcessor {

    public abstract GstSheet getGstSheetObj();

    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
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
        for (int i = 0; i < gstSheet.getRowPairCount(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                gstSheet.getRowPairs().add(new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1))));
            } else {
                gstSheet.getRowPairs().add(new DataPair());
            }
        }
    }

    private void readColumnPairs(Sheet sheet, GstSheet gstSheet) {
        if (gstSheet.getCpRow() == -1) {
            System.out.println("Skipping the column pairs as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row labelRow = sheet.getRow(gstSheet.getCpRow());
        if (labelRow != null) {
            for (int i = 0; i < gstSheet.getColumnPairCount(); i++) {
                String val = Helper.getCellValueAsString(labelRow.getCell(i));
                if (StringUtils.isEmpty(val)) {
                    gstSheet.getColumnPairs().add(new DataPair());
                } else {
                    gstSheet.getColumnPairs().add(new DataPair().setLabel(val));
                }
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
                String val = Helper.getCellValueAsString(summrayRow.getCell(i));
                if (gstSheet.isSummaryInLastRow()) {
                    if (i == 0 || StringUtils.isEmpty(val)) {
                        gstSheet.getSummaryList().add(new DataPair().setLabel(val));
                    } else {
                        gstSheet.getSummaryList().add(new DataPair().setValue(val));
                    }
                } else {
                    if (StringUtils.isEmpty(val)) {
                        gstSheet.getSummaryList().add(new DataPair().setLabel(val));
                    } else {
                        gstSheet.getSummaryList().add(new DataPair().setValue(val));
                    }
                }

            }
        }
    }

    private void readTableHeaders(Sheet sheet, GstSheet gstSheet) {
        Row row = sheet.getRow(gstSheet.getDataStartRow() - 1);
        for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
            gstSheet.getTableHeaders().add(new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(i))));
        }
    }

    private void readRecords(Sheet sheet, GstSheet gstSheet) {
        Row row = null;
        for (int rc = gstSheet.getDataStartRow(); ; rc++) {
            row = sheet.getRow(rc);
            List<String> cellDataList = new LinkedList<>();
            if (rc > sheet.getLastRowNum()) {
                return;
            }
            if (row == null) {
                cellDataList.add(null);
            } else {
                for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
                    String val = Helper.getCellValueAsString(row.getCell(i));
                    if (gstSheet.isSummaryInLastRow() && i == 1 && StringUtils.isEmpty(val)) {
                        return;
                    } else {
                        cellDataList.add(val);
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
        LinkedList<DataPair> summaryList = finalSheet.getSummaryList();
        if (gstSheets.size() > 1) {
            if (finalSheet.getRecords() == null) {
                finalSheet.setRecords(new LinkedList<>());
            }
            for (int i = 1; i < gstSheets.size(); i++) {
                GstSheet sheet = gstSheets.get(i);
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                mergeSummary(sheet, summaryList);
            }
        }
        computeUniqueCounts(finalSheet);
        return finalSheet;
    }

    protected void mergeSummary(GstSheet sheet, LinkedList<DataPair> summaryList) {
        for (int j = 0; j < sheet.getSummaryList().size(); j++) {
            DataPair dataPair = sheet.getSummaryList().get(j);
            if (StringUtils.isNotEmpty(dataPair.getValue())) {
                DataPair finalPair = summaryList.get(j);
                finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue())));
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
        for (DataPair dataPair : gstSheet.getRowPairs()) {
            cc = 0;
            Row row = sheet.createRow(rc++);
            if (StringUtils.isNotEmpty(dataPair.getLabel())) {
                Cell cell = row.createCell(cc++);
                cell.setCellValue(dataPair.getLabel());
            }
            if (StringUtils.isNotEmpty(dataPair.getValue())) {
                Cell cell = row.createCell(cc);
                cell.setCellValue(dataPair.getValue());
            }
        }
    }

    private void writeColumnPairs(Sheet sheet, GstSheet gstSheet) {
        if (gstSheet.getCpRow() == -1 && CollectionUtils.isEmpty(gstSheet.getColumnPairs())) {
            System.out.println("Skipping the column pairs as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = sheet.createRow(gstSheet.getCpRow());
        int cc = 0;
        for (DataPair dataPair : gstSheet.getColumnPairs()) {
            Cell cell = row.createCell(cc++);
            if (StringUtils.isNotEmpty(dataPair.getLabel())) {
                cell.setCellValue(dataPair.getLabel());
            } else {
                cell.setBlank();
            }
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
            if (dataPair == null || StringUtils.isBlank(dataPair.getValue())) {
                cell.setBlank();
            } else {
                cell.setCellValue(dataPair.getValue());
            }
        }
    }

    private void writeTableHeaders(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getTableHeaders())) {
            System.out.println("Skipping the header row as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = sheet.createRow(gstSheet.getDataStartRow() - 1);
        for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(gstSheet.getTableHeaders().get(i).getLabel());
        }
    }

    private void writeRecords(Sheet sheet, GstSheet gstSheet) {
        if (CollectionUtils.isEmpty(gstSheet.getRecords())) {
            System.out.println("Skipping the table data as it is not defined for sheet : " + sheet.getSheetName());
            return;
        }
        Row row = null;
        for (int i = 0; i < gstSheet.getRecords().size(); i++) {
            row = sheet.createRow(i + gstSheet.getDataStartRow());
            List<String> cellDataList = gstSheet.getRecords().get(i);
            if (CollectionUtils.isEmpty(cellDataList)) {
                continue;
            }
            for (int j = 0; j < gstSheet.getHeaderCount(); j++) {
                Cell cell = row.createCell(j);
                String str = cellDataList.get(j);
                if (StringUtils.isBlank(str)) {
                    cell.setBlank();
                } else {
                    cell.setCellValue(str);
                }
            }
        }
    }
}
