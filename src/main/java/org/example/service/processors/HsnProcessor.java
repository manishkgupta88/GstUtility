package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.model.HsnSheet;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */

public class HsnProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        HsnSheet sheetObj = new HsnSheet();
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
        HsnSheet finalSheet = (HsnSheet) gstSheets.get(0);
        LinkedList<DataPair> summaryList = finalSheet.getSummaryList();
        if (gstSheets.size() > 1) {
            if (finalSheet.getRecords() == null) {
                finalSheet.setRecords(new LinkedList<>());
            }
            Map<String, List<String>> map = getRecordMap(finalSheet.getRecords());
            for (int i = 1; i < gstSheets.size(); i++) {
                HsnSheet sheet = (HsnSheet) gstSheets.get(i);
                mergeRecordList(sheet, map);
                mergeSummary(sheet, summaryList);
            }
            finalSheet.getRecords().clear();
            finalSheet.getRecords().addAll(map.values());
        }
        computeUniqueCounts(finalSheet);
        return finalSheet;
    }

    private Map<String, List<String>> getRecordMap(List<List<String>> records) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (List<String> record : records) {
            if (CollectionUtils.isNotEmpty(record)) {
                String key = getRecordMapKey(record);
                map.put(key, record);
            }
        }
        return map;
    }

    private String getRecordMapKey(List<String> record) {
        if (record.size() > 5) {
            return (record.get(0) + ":" + record.get(5));
        }
        return record.get(0);
    }

    private void mergeRecordList(HsnSheet sheet, Map<String, List<String>> map) {
        if (sheet.getRecords() == null) {
            return;
        }
        for (List<String> record : sheet.getRecords()) {
            if (record == null) {
                continue;
            }
            String key = getRecordMapKey(record);
            List<String> mapList = map.get(key);
            if (mapList == null) {
                map.put(key, record);
                continue;
            }
            for (int k = 3; k < record.size(); k++) {
                if (k == 5) {
                    continue;
                }
                double recordVal = NumberUtils.toDouble(record.get(k));
                double mapListVal = NumberUtils.toDouble(mapList.get(k));
                mapList.add(k, String.valueOf(recordVal + mapListVal));
            }
        }
    }


    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
