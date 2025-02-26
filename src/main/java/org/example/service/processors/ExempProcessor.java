package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.ExempSheet;
import org.example.model.GstSheet;

import java.util.*;

/**
 * User : Manish K. Gupta
 */

public class ExempProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        ExempSheet sheetObj = new ExempSheet();
        readRowPairs(sheet, sheetObj);
        readColumnPairs(sheet, sheetObj);
        readSummary(sheet, sheetObj);
        readTableHeaders(sheet, sheetObj);
        readRecords(sheet, sheetObj);
        return sheetObj;
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        ExempSheet finalSheet = (ExempSheet) gstSheets.get(0);
        LinkedList<DataPair> summaryList = finalSheet.getSummaryList();
        if (gstSheets.size() > 1) {
            if (finalSheet.getRecords() == null) {
                finalSheet.setRecords(new ArrayList<>());
            }
            Map<String, List<String>> map = getRecordMap(finalSheet.getRecords());
            for (int i = 1; i < gstSheets.size(); i++) {
                ExempSheet sheet = (ExempSheet) gstSheets.get(i);
                mergeRecordList(sheet, map);
                mergeSummary(sheet, summaryList);
            }
            finalSheet.getRecords().clear();
            finalSheet.getRecords().addAll(map.values());
        }
        return finalSheet;
    }

    private void mergeRecordList(ExempSheet sheet, Map<String, List<String>> map) {
        if (sheet.getRecords() == null) {
            return;
        }
        for (List<String> record : sheet.getRecords()) {
            if (record != null) {
                continue;
            }
            List<String> mapList = map.get(record.get(0));
            if (mapList == null) {
                map.put(record.get(0), record);
                continue;
            }
            for (int k = 1; k < record.size(); k++) {
                double recordVal = NumberUtils.toDouble(record.get(k));
                double mapListVal = NumberUtils.toDouble(mapList.get(k));
                mapList.add(k, String.valueOf(recordVal + mapListVal));
            }
        }
    }

    private void mergeSummary(ExempSheet sheet, LinkedList<DataPair> summaryList) {
        for (int j = 0; j < sheet.getSummaryList().size(); j++) {
            DataPair dataPair = sheet.getSummaryList().get(j);
            if (StringUtils.isNotEmpty(dataPair.getValue())) {
                DataPair finalPair = summaryList.get(j);
                finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue(), 0.0)));
            }
        }
    }

    private Map<String, List<String>> getRecordMap(List<List<String>> records) {
        Map<String, List<String>> map = new HashMap<>();
        for (List<String> record : records) {
            if (record != null) {
                map.put(record.get(0), record);
            }
        }
        return map;
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
