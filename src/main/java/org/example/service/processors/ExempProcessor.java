package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.model.DataPair;
import org.example.model.ExempSheet;
import org.example.model.GstSheet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */

public class ExempProcessor extends AbstractExcelProcessor {
    @Override
    public GstSheet getGstSheetObj() {
        return new ExempSheet();
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
                finalSheet.setRecords(new LinkedList<>());
            }
            Map<String, LinkedList<DataPair>> map = getRecordMap(finalSheet.getRecords());
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

    private Map<String, LinkedList<DataPair>> getRecordMap(List<LinkedList<DataPair>> records) {
        Map<String, LinkedList<DataPair>> map = new HashMap<>();
        for (LinkedList<DataPair> record : records) {
            if (record != null) {
                map.put(record.get(0).getValue(), record);
            }
        }
        return map;
    }

    private void mergeRecordList(ExempSheet sheet, Map<String, LinkedList<DataPair>> map) {
        if (sheet.getRecords() == null) {
            return;
        }
        for (LinkedList<DataPair> record : sheet.getRecords()) {
            if (record != null) {
                continue;
            }
            LinkedList<DataPair> mapList = map.get(record.get(0).getValue());
            if (mapList == null) {
                map.put(record.get(0).getValue(), record);
                continue;
            }
            for (int k = 1; k < record.size(); k++) {
                double recordVal = NumberUtils.toDouble(record.get(k).getValue());
                double mapListVal = NumberUtils.toDouble(mapList.get(k).getValue());
                mapList.set(k, mapList.get(k).setValue(String.valueOf(recordVal + mapListVal)));
            }
        }
    }

    private void mergeSummary(ExempSheet sheet, LinkedList<DataPair> summaryList) {
        for (int j = 0; j < sheet.getSummaryList().size(); j++) {
            DataPair dataPair = sheet.getSummaryList().get(j);
            if (StringUtils.isNotEmpty(dataPair.getValue())) {
                DataPair finalPair = summaryList.get(j);
                finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue())));
            }
        }
    }
}
