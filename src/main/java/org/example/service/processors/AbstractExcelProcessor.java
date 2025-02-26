package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public abstract class AbstractExcelProcessor implements IExcelProcessor {

    protected void readRowPairs(Sheet sheet, GstSheet sheetObj) {
        for (int i = 0; i < sheetObj.getRowPairCount(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                sheetObj.getRowPairs().add(new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1))));
            } else {
                sheetObj.getRowPairs().add(new DataPair());
            }
        }
    }

    protected void readColumnPairs(Sheet sheet, GstSheet sheetObj) {
        Row labelRow = sheet.getRow(sheetObj.getCpRow());
        if (labelRow != null) {
            for (int i = 0; i < sheetObj.getColumnPairCount(); i++) {
                String val = Helper.getCellValueAsString(labelRow.getCell(i));
                if (StringUtils.isEmpty(val)) {
                    sheetObj.getTableHeaders().add(new DataPair());
                } else {
                    sheetObj.getRowPairs().add(new DataPair().setLabel(val));
                }
            }
        }
    }

    protected void readSummary(Sheet sheet, GstSheet sheetObj) {
        int rc = sheetObj.getSummaryRow();
        if (sheetObj.isSummaryInLastRow()) {
            rc = sheetObj.getDataStartRow() + sheetObj.getRecords().size();
        }
        Row summrayRow = sheet.getRow(rc);
        if (summrayRow != null) {
            for (int i = 0; i < sheetObj.getColumnPairCount(); i++) {
                String val = Helper.getCellValueAsString(summrayRow.getCell(i));
                if (sheetObj.isSummaryInLastRow()) {
                    if (i == 0 || StringUtils.isEmpty(val)) {
                        sheetObj.getSummaryList().add(new DataPair().setLabel(val));
                    } else {
                        sheetObj.getSummaryList().add(new DataPair().setValue(val));
                    }
                } else {
                    if (StringUtils.isEmpty(val)) {
                        sheetObj.getTableHeaders().add(new DataPair());
                    } else {
                        sheetObj.getRowPairs().add(new DataPair().setValue(val));
                    }
                }

            }
        }
    }

    protected void readTableHeaders(Sheet sheet, GstSheet sheetObj) {
        Row row = sheet.getRow(sheetObj.getDataStartRow() - 1);
        for (int i = 0; i < sheetObj.getHeaderCount(); i++) {
            sheetObj.getTableHeaders().add(new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(i))));
        }
    }

    protected void readRecords(Sheet sheet, GstSheet sheetObj) {
        Row row = null;
        for (int rc = sheetObj.getDataStartRow(); ; rc++) {
            row = sheet.getRow(rc);
            List<String> cellDataList = new ArrayList<>();
            for (int i = 0; i < sheetObj.getHeaderCount(); i++) {
                if (row == null) {
                    return;
                }
                String val = Helper.getCellValueAsString(row.getCell(i));
                if (sheetObj.isSummaryInLastRow() && i == 1 && StringUtils.isEmpty(val)) {
                    return;
                } else {
                    cellDataList.add(val);
                }
            }
            sheetObj.getRecords().add(cellDataList);
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
                finalSheet.setRecords(new ArrayList<>());
            }
            for (int i = 1; i < gstSheets.size(); i++) {
                GstSheet sheet = gstSheets.get(i);
                if (sheet.getRecords() != null) {
                    finalSheet.getRecords().addAll(sheet.getRecords());
                }
                for (int j = 0; j < sheet.getSummaryList().size(); j++) {
                    DataPair dataPair = sheet.getSummaryList().get(j);
                    if (StringUtils.isNotEmpty(dataPair.getValue())) {
                        DataPair finalPair = summaryList.get(j);
                        finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue())));
                    }
                }
            }
        }
        return finalSheet;
    }
}
