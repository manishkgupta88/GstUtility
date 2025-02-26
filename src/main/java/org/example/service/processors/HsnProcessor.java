package org.example.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.model.HsnSheet;

import java.util.*;

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

        /*while (itr.hasNext()) {
            row = itr.next();
            HsnRecord record = parseInvoiceRow(row);
            records.add(record);
            ItemHsnKey itemHsnKey = sheetObj.getItemHsnKey(record.getHsn(), record.getRate());
            ItemHsn prevItemHsn = sheetObj.getHsnMap().get(itemHsnKey);
            if (prevItemHsn == null) {
                prevItemHsn = record.getItemHsn();
            } else {
                ItemHsn itemHsn = record.getItemHsn();
                prevItemHsn.merge(itemHsn);
            }
            sheetObj.getHsnMap().put(itemHsnKey, prevItemHsn);
        }
        sheetObj.setRecords(records);*/
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
                finalSheet.setRecords(new ArrayList<>());
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
        return finalSheet;
    }

    private Map<String, List<String>> getRecordMap(List<List<String>> records) {
        Map<String, List<String>> map = new HashMap<>();
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
            if (record != null) {
                continue;
            }
            List<String> mapList = map.get(record.get(0));
            if (mapList == null) {
                map.put(getRecordMapKey(record), record);
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

    private void mergeSummary(HsnSheet sheet, LinkedList<DataPair> summaryList) {
        for (int j = 0; j < sheet.getSummaryList().size(); j++) {
            DataPair dataPair = sheet.getSummaryList().get(j);
            if (StringUtils.isNotEmpty(dataPair.getValue())) {
                DataPair finalPair = summaryList.get(j);
                finalPair.setValue(String.valueOf(NumberUtils.toDouble(dataPair.getValue()) + NumberUtils.toDouble(finalPair.getValue())));
            }
        }
    }

    @Override
    public void write(Sheet wbSheet, GstSheet gstSheet) {

    }
}
