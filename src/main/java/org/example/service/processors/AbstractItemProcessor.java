package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.model.DataPair;
import org.example.model.GstSheet;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */

public abstract class AbstractItemProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        if (CollectionUtils.isEmpty(gstSheets)) {
            return null;
        }
        GstSheet finalSheet = gstSheets.get(0);
        LinkedList<DataPair> summaryList = finalSheet.getSummaryList();
        mergeRecords(gstSheets, finalSheet, summaryList);
        mergeSummary(gstSheets, finalSheet);
        computeUniqueCounts(finalSheet);
        return finalSheet;
    }

    private void mergeRecords(List<GstSheet> gstSheets, GstSheet finalSheet, LinkedList<DataPair> summaryList) {
        if (gstSheets.size() <= 1) {
            return;
        }
        if (finalSheet.getRecords() == null) {
            finalSheet.setRecords(new LinkedList<>());
        }
        Map<String, LinkedList<DataPair>> map = getRecordMap(finalSheet.getRecords());
        for (int i = 1; i < gstSheets.size(); i++) {
            GstSheet sheet = gstSheets.get(i);
            mergeRecordList(sheet, map);
        }
        finalSheet.getRecords().clear();
        finalSheet.getRecords().addAll(map.values());
    }

    private Map<String, LinkedList<DataPair>> getRecordMap(LinkedList<LinkedList<DataPair>> records) {
        Map<String, LinkedList<DataPair>> map = new LinkedHashMap<>();
        for (LinkedList<DataPair> record : records) {
            if (CollectionUtils.isNotEmpty(record)) {
                mergeMapList(map, record);
            }
        }
        return map;
    }

    private String getRecordMapKey(LinkedList<DataPair> record) {
        if (record.size() > 5) {
            return (record.get(0).getValue() + ":" + record.get(5).getValue());
        }
        return record.get(0).getValue();
    }

    private void mergeRecordList(GstSheet sheet, Map<String, LinkedList<DataPair>> map) {
        if (sheet.getRecords() == null) {
            return;
        }
        for (LinkedList<DataPair> record : sheet.getRecords()) {
            if (record == null) {
                continue;
            }
            mergeMapList(map, record);
        }
    }

    private void mergeMapList(Map<String, LinkedList<DataPair>> map, LinkedList<DataPair> record) {
        String key = getRecordMapKey(record);
        LinkedList<DataPair> mapList = map.get(key);
        if (mapList == null) {
            map.put(key, record);
            return;
        }
        for (int k = 3; k < record.size(); k++) {
            if (k == 5) {
                continue;
            }
            double recordVal = NumberUtils.toDouble(record.get(k).getValue());
            double mapListVal = NumberUtils.toDouble(mapList.get(k).getValue());
            mapList.set(k, mapList.get(k).setValue(String.valueOf(recordVal + mapListVal)));
        }
    }
}
