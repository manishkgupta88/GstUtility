package org.manitech.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.manitech.model.DataPair;
import org.manitech.model.GstR1Report;
import org.manitech.model.GstSheet;

import java.util.LinkedList;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor extends AbstractExcelProcessor {
    private static final Logger logger = LogManager.getLogger(Gst1ReportProcessor.class);

    @Override
    public GstSheet getGstSheetObj() {
        return new GstR1Report();
    }

    @Override
    public void sheetMerge(GstSheet gstSheet) {
        if (gstSheet == null || CollectionUtils.isEmpty(gstSheet.getRecords())) {
            return;
        }
        logger.info("Updating the records");
        for (LinkedList<DataPair> record : gstSheet.getRecords()) {
            if (CollectionUtils.isEmpty(record)) {
                continue;
            }
            for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
                if (i == 14 && record.size() > 14) {
                    DataPair dataPair = record.get(14);
                    dataPair.setValue("Delhi");
                }
            }
        }
    }
}
